package com.example.codebaseandroidapp.ui

import android.util.Log
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoReactiveExtensionBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import java.io.Serializable
import java.util.concurrent.TimeUnit

/*
1. Reactive Programing
Reactive Programing là một phương pháp lập trình tập trung vào các luồng dữ liệu không đồng bộ và quan sát sự
thay đổi của các luồng dữ liệu không đồng bộ đó, khi có sự thay đổi sẽ có hành động xử lý phù hợp. Vì đây là
luồng dữ liệu không đồng bộ nên các module code cùng lúc chạy trên các thread khác nhau từ đó rút ngắn thời gian
thực thi mà không làm block main thread.

Một số thuật ngữ liên quan:
- synchronous: đồng bộ, tuần tự
- asynchronous: không đồng bộ, bất đồng bộ

Lấy một ví dụ đơn giản là x = y + z, trong đó tổng của y và z được gán cho x. Trong reactive programming,
khi giá trị y thay đổi thì x cũng tự động thay đổi theo mà không cần phải thực hiện lại câu lệnh x = y + z.
Điều này có thể nhận được khi ta lắng nghe, quan sát giá trị của y và z.

2. Reactive Extension
Reactive Extension (ReactiveX hay RX) là một thư viện follow theo những quy tắc của Reactive Programming tức là
nó soạn ra các chương trình bất đồng bộ và dựa trên sự kiện bằng cách sử dụng các chuỗi quan sát được. Các thư viện
này cung cấp một list các interface và method giúp các developers viết code một cách đơn giản và sáng sủa hơn.

Rx là sự kết hợp của những ý tưởng tốt nhất từ Observer pattern, Iterator pattern và functional programming.

Reactive Extension có trên rất nhiều ngôn ngữ khác nhau như C++ (RxCpp), C# (Rx.NET), Java (RxJava),
Kotlin (RxKotlin) Swift (RxSwift), ...

3. RxJava
* Khái niệm: Reactive Extension (ReactiveX hay RX) sử dụng ngôn ngữ Java → RxJava

* Những thành phần quan trọng trong RxJava:
Về cơ bản RxJava có hai thành phần chính: Observable và Observer
Thêm vào đó, có những thứ khác như Schedulers, Operators và Subscription là các thành phần đóng vai trò như
đa luồng, thao tác dữ liệu và kết nối.

Chúng ta sẽ cùng làm quen với từng thành phần:
Observable: Là luồng dữ liệu thực hiện một số công việc và phát ra dữ liệu.
Observer : Là thành phần đi kèm không thể thiếu của Observable. Nó nhận dữ liệu được phát ra bởi Observable.
Subcription: Là mối liên kết giữa Observable và Observer. Có thể có nhiều Observer đăng ký một Observable duy nhất.
Operator: Hỗ trợ cho việc sửa đổi dữ liệu được phát ra bởi Observable trước khi Observer nhận chúng.
Schedulers: Scheduler quyết định thread mà Observable sẽ phát ra dữ liệu và trên thread nào Observer sẽ nhận
dữ liệu.
Disposable : Disposable được sử dụng để hủy sự kết nối của Observer với Observable khi không còn cần thiết việc
này rất hữu dụng để tránh việc rò rỉ bộ nhớ.

* Các loại Observable và Observer
Chúng ta có 5 loại Observable đi kèm là 5 loại Observer tương ứng
Observable  - Observer
Single - SingleObserver
Maybe - MaybeObserver
Flowable - Observer
Completable - CompletableObserver

4. RxAndroid
RxAndroid được đặc biệt sử dụng cho nền tảng Android được phát triển dựa trên RxJava. Đặc biệt Schedulers
được bổ sung cho RxAndroid nhằm hỗ trợ cho đa luồng trong ứng dụng Android. Schedulers sẽ giúp bạn phân chia luồng
chạy cho từng module code sao cho phù hợp.

Một vài luồng chạy phổ biến được sử dụng qua Schedulers.
Schedulers.io(): Được sử dụng để thực hiện các hoạt động không tốn nhiều CPU như thực hiện cuộc gọi mạng,
đọc đĩa/tệp, thao tác cơ sở dữ liệu, v.v.
AndroidSchedulers.mainThread(): Cung cấp quyền truy cập vào Android Main Thread/UI Thread.
Schedulers.newThread(): Thread mới sẽ được tạo ra mỗi khi một nhiệm vụ được tạo

5. Các cách tạo observable và observer
- Observable.create()
Với create(), bạn có thể tự thiết kế hoạt động của Observer bằng cách gọi các phương thức onError, onNext,
và onCompleted một cách thích hợp. Lưu ý là Subscriber.onComplete() hoặc Subscriber.onError() chỉ được gọi
duy nhất 1 lần và sau đó không được gọi thêm bất cứ hàm nào khác của Observer.

- Observable.fromArray()
fromArray chuyển đổi một list (mảng) object dữ liệu nào đó thành một Observable. Sau đó Observable sẽ phát ra
lần lượt các item đó, và chúng được xử lí trong onNext.Sau khi hoàn thành sẽ gọi đến onCompleted().

- Observable.just()
just() chuyển đổi một object hoặc một tập hợp các object thành Observable và phát ra nó. Với just(), giả sử nếu
truyền vào 1 array thì array đó sẽ được chuyển đổi thành Observable và phát ra chính array đó.

- Observable.interval()
interval() tạo một Observable phát ra một chuỗi các số nguyên cách nhau một khoảng thời gian cụ thể.

- Observable.timer()
timer() tạo 1 Observable sẽ phát ra chỉ 1 item sau 1 khoảng thời gian delay cho trước.

- Observable.range()
Range() tạo 1 Observable từ 1 dải interger và lần lượt phát ra các interger trong dải đó.

- Observable.range()
Range() tạo 1 Observable từ 1 dải interger và lần lượt phát ra các interger trong dải đó.

- Observable.repeat()
Repeat() tạo 1 Observable mà có thể lặp đi lặp lại việc phát ra dữ liệu. Bạn có thể hạn chế
số lần lặp lại bằng cách set repeat(số lần lặp).

6. Sự khác nhau giựa map và flat
flatMap và map là hai phương thức quan trọng trong lập trình với Reactive Extensions (Rx) và RxJava/RxKotlin.
Mặc dù cả hai đều được sử dụng để xử lý dữ liệu trên Observable, chúng có một số điểm khác biệt chính:

- map: map dùng để biến đổi mỗi phần tử trong Observable thành một phần tử mới dựa trên một quy tắc biến đổi
cụ thể. Kết quả là một Observable mới có cùng số lượng phần tử với Observable ban đầu.

- flatMap: flatMap cũng biến đổi mỗi phần tử trong Observable thành một phần tử mới, nhưng thay vì trả về một
phần tử, nó trả về một Observable. Điều này cho phép bạn thực hiện biến đổi phức tạp hơn hoặc trả về nhiều
phần tử trong một lần.

Vây sự khác biệt giữa map và flatMap phụ thuộc vào yêu cầu cụ thể của bạn. Nếu bạn muốn biến đổi từng phần tử
thành một phần tử mới, sử dụng map. Nếu bạn muốn biến đổi và tạo ra một loạt các phần tử mới hoặc làm việc với
nhiều Observable khác nhau, hãy sử dụng flatMap.

7. merge, zip
- Merge (Observable.merge):
merge được sử dụng để kết hợp các luồng dữ liệu thành một luồng duy nhất.
Nó không quan tâm đến thời điểm phát ra dữ liệu từ các luồng gốc. Dữ liệu từ bất kỳ luồng nào sẵn sàng
sẽ được đưa vào luồng kết quả ngay lập tức.
Kết quả có thể chứa dữ liệu từ các luồng gốc theo thứ tự ngẫu nhiên.

- Zip (Observable.zip):
zip được sử dụng để kết hợp dữ liệu từ các luồng thành các cặp dữ liệu tương ứng.
Nó yêu cầu tất cả các luồng phải phát ra dữ liệu cùng một lúc để tạo cặp dữ liệu.
Kết quả có thể chứa các cặp dữ liệu được kết hợp từ các luồng gốc theo thứ tự tương ứng.

ví dụ: chúng ta sử dụng Observable.zip để gọi hai API callApi1() và callApi2() song song. Khi cả hai API đều
hoàn thành, hàm BiFunction sẽ được gọi để kết hợp kết quả từ hai API thành một đối tượng CombinedResult.
Sau đó,bạn có thể xử lý kết quả hoặc hiển thị nó.

=> Nói cách khác, merge gộp các luồng mà không quan tâm đến thời điểm phát ra, trong khi zip kết hợp dữ liệu
 tương ứng từ các luồng theo thứ tự.
*/
@AndroidEntryPoint
class DemoReactiveExtensionFragment :
    OthersBaseFragment<FragmentDemoReactiveExtensionBinding>(FragmentDemoReactiveExtensionBinding::inflate) {

    private var disposable: Disposable? = null
    private var user : User? = null
    companion object {
        @JvmStatic fun newInstance() = DemoReactiveExtensionFragment()
    }

    override fun initObserve() {
//        createObservable().subscribeOn(io()).observeOn(AndroidSchedulers.mainThread())
//            .subscribe(createSerializableObserver())

//        var observable = createObservable()
//        user?.name = "hieu day roi 1"
//        observable.subscribeOn(io()).observeOn(AndroidSchedulers.mainThread())
//            .subscribe(createSerializableObserver())

//        demoMap()
//        demoFlatmap()
//        demoMerge()
        demoZip()
    }

    override fun initialize() {
    }

    private fun getListUser(): ArrayList<User> {
        val list = ArrayList<User>()
        list.add(User("0328562861", "Hieu", "eHieu"))
        list.add(User("0328562862", "Thao", "eThao"))
        list.add(User("0328562863", "Nhi", "eNhi"))
        list.add(User("0328562864", "Chau", "eChau"))
        return list;
    }

    private fun createObservable() : Observable<Serializable> {
        val list = getListUser()
        val users : Array<User> = arrayOf(
            User("0328562861", "Hieu", "eHieu"),
            User("0328562862", "Hieu 2", "eHieu")
        )
        user = User("0328562861", "Hieu", "eHieu")
        val user1 = User("0328562861", "Hieu 1", "eHieu")
        val user2 = User("0328562861", "Hieu 2", "eHieu")
        /*
        *  Create Observable with create()
        * */
//        return Observable.create {
//            if(!it.isDisposed) {
//                if(list.isNullOrEmpty()) {
//                    it.onError(Exception())
//                }
//                if(!list.isEmpty()) {
//                    for (item in list) {
//                        Thread.sleep(500)
//                        it.onNext(item)
//                        Log.d("Thread",Thread.currentThread().name)
//                    }
//                }
//                Thread.sleep(500)
//                it.onComplete()
//            }
//        }

        /*
        *  Create Observable with fromArray()
        * */
//        return Observable.fromArray(*users)

        /*
        *  Create Observable with just()
        * */
        return Observable.just(users)

        /*
        * Create Observable with interval
         */
//        return Observable.interval(3,5, TimeUnit.SECONDS)

        /*
        * Create Observable with timer
         */
//        return Observable.timer(5, TimeUnit.SECONDS)

        /*
        * Create Observable with range
         */
//        return Observable.rangeLong(0, 10)

        /*
        * Create Observable with repeat
         */
//        return Observable.rangeLong(0, 6).repeat(2)

        /*
        * Create Observable with defer
         */
//        return user!!.getNameObservable()
    }

    private fun createSerializableObserver() = object: DemoReactiveExtensionObservable<Serializable>(binding) {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            disposable = d
        }

        override fun onNext(t: Serializable) {
            var info = "${binding.tvReactive.text}"
            when (t) {
                is User -> {
                    info += "\n\n ${t.toString()}"
                }
                is ArrayList<*> -> {
                    info += "\n"
                    for (item in t) {
                        info += "\n $item"
                    }
                }
                is Array<*> -> {
                    info += "\n"
                    for (item in t) {
                        info += "\n ${item.toString()}"
                    }
                }
                is String -> {
                    info += "\n\n $t"
                }
                is Int -> {
                    info += "\n\n $t"
                }
            }
            binding.tvReactive.text = info
        }
    }

    private fun createLongObserver() = object: DemoReactiveExtensionObservable<Long>(binding) {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            disposable = d
        }

        override fun onNext(t: Long) {
            if(t == 6L) {
                disposable?.dispose()
            }
            val info = "${binding.tvReactive.text} \n\n ${t.toString()}"
            binding.tvReactive.text = info
        }
    }

    private fun createIntObserver() = object: DemoReactiveExtensionObservable<Int>(binding) {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            disposable = d
        }

        override fun onNext(t: Int) {
            val info = "${binding.tvReactive.text} \n\n ${t.toString()}"
            binding.tvReactive.text = info
        }
    }

    private fun <T> createMutableListObserver() = object: DemoReactiveExtensionObservable<List<T>>(binding) {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            disposable = d
        }

        override fun onNext(t: List<T>) {
            var info = "${binding.tvReactive.text}"
            info += "\n"
            for (item in t) {
                info += "\n ${item.toString()}"
            }
            binding.tvReactive.text = info
        }
    }

    private fun demoMerge() {
        val source1 = Observable.interval(1, TimeUnit.SECONDS)
            .map { "Source 1: $it" }
            .take(5)
            .subscribeOn(io())

        val source2 = Observable.interval(500, TimeUnit.MILLISECONDS)
            .map { "Source 2: $it" }
            .take(5)
            .subscribeOn(io())

        Observable.merge(source1, source2)
            .subscribe { println("Merge received: $it") }
    }

    private fun demoZip() {
        val source1 = Observable.interval(1, TimeUnit.SECONDS)
            .take(5)
            .subscribeOn(io())

        val source2 = Observable.interval(500, TimeUnit.MILLISECONDS)
            .take(5)
            .subscribeOn(io())

        val source3 = Observable.interval(500, TimeUnit.MILLISECONDS)
            .map { it -> it%2==0L }
            .take(5)
            .subscribeOn(io())

        Observable.zip(
            source1,
            source2,
            source3
        ) { t1, t2, t3 -> "$t1-$t2-$t3" }
        .subscribe { println("Zip received: $it") }
    }

    private fun demoMap() {
        val numbers = Observable.just(1, 2, 3, 4, 5)
        numbers.map { it * it }.subscribeOn(io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(createIntObserver())
    }

    //Một danh sách bài viết cho tất cả người dùng
    private fun demoFlatmap() {
        getUsers()
            .flatMap { users ->
                println("Danh sách Users: $users")
                Observable.fromIterable(users)
                    .flatMap { user ->
                        getPostsByUserId(user.phone.toInt())
                    }
            }
            .toList()
            .subscribe { posts ->
                println("Danh sách bài viết: $posts")
            }
    }

    // Hàm mô phỏng việc gọi API để lấy danh sách người dùng
    fun getUsers(): Observable<List<User>> {
        val users = listOf(
            User("1", "Alice", "eAlice"),
            User("2", "Bob", "eBob"),
            User("3", "Charlie", "eCharlie")
        )
        return Observable.just(users)
    }

    // Hàm mô phỏng việc gọi API để lấy danh sách bài viết dựa trên userId
    fun getPostsByUserId(userId: Int): Observable<List<Post>> {
        val posts = when (userId) {
            1 -> listOf(
                Post(1, "Bài viết 1"),
                Post(1, "Bài viết 2")
            )
            2 -> listOf(
                Post(2, "Bài viết 3")
            )
            3 -> listOf(
                Post(3, "Bài viết 4"),
                Post(3, "Bài viết 5"),
                Post(3, "Bài viết 6")
            )
            else -> emptyList()
        }
        return Observable.just(posts)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let {
            if(!it.isDisposed) {
                it.dispose()
            }
        }
    }
}

open class DemoReactiveExtensionObservable<T : Any>(val binding: FragmentDemoReactiveExtensionBinding)
    : MyObservable<T>(binding) {
    override fun onSubscribe(d: Disposable) {
        binding.tvReactive.text = ""
        val info = "onSubscribe"
        binding.tvReactive.text = info
    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {
        val info = "onError"
        binding.tvReactive.text = info
    }

    override fun onComplete() {
        val info = "${binding.tvReactive.text} \n\n onComplete" +
                "\n\nThread: ${Thread.currentThread().name}"
        binding.tvReactive.text = info
    }
}

abstract class MyObservable<T : Any>(val viewBinding: ViewBinding) : Observer<T>

data class Post(val userId: Int, val title: String)

