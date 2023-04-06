package com.tt.ktdemo.copy

import android.util.Log

class ClassSuper {
    //Kotlin写法
//    class MyViewKotlin : View {
//        constructor(context: Context) : super(context)
//        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
//        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//            context,
//            attrs,
//            defStyleAttr
//        )
//
//        constructor(
//            context: Context,
//            attrs: AttributeSet?,
//            defStyleAttr: Int,
//            defStyleRes: Int
//        ) : super(
//            context,
//            attrs,
//            defStyleAttr,
//            defStyleRes
//        )
//
//        init {
//            //do something
//        }
//    }
//
//    //Java写法
//    public class MyViewJava extends View {
//        public MyViewJava(Context context) {
//            super(context);
//            init();
//        }
//
//        public MyViewJava(Context context, @Nullable AttributeSet attrs) {
//            super(context, attrs);
//            init();
//        }
//
//        public MyViewJava(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            init();
//        }
//
//        public MyViewJava(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//            super(context, attrs, defStyleAttr, defStyleRes);
//            init();
//        }
//
//        private void init() {
//            //do something
//        }
//    }


}


//案例1，没有主构造函数，只有一个默认的无参构造函数
class Test1 {
}

//案例2，有一个主构造函数，默认的无参构造函数无法使用
class Test2 constructor(name: String) {
    var name: String = name
}

//案例3，只有一个主构造函数，如果构造函数没有任何修改符（例如private）或注解，那么constructor关键字可以省略
class Test3(name: String) {
    var name: String = name
}

//案例4，有一个主构造函数和一个次构造函数
class Test4(name: String) {
    var nameT: String = name
    var ageT: Int? = null

    //次级构造函数
    constructor(name: String, age: Int) : this(name) {
        ageT = age
    }
}

//案例5，只有一个次构造函数
class Test5 {
    var nameT: String? = null
    var ageT: Int? = null

    constructor(name: String, age: Int) {
        nameT = name
        ageT = age
    }
}

class Test6 constructor(name: String, age: Int) {
    //主构造函数的属性赋值，也可以通过init代码块赋值
    var nameT: String = name
    var ageT: Int? = age

    var sexT: Int? = null

    //
    constructor(name: String, age: Int, sex: Int) : this(name, age) {
        //次构造函数的赋值
        sexT = sex
    }
}

class Test7 constructor(name: String, age: Int) {

    //    5.1如果定义了主构造函数，那么在init代码块中可以调用主构造函数的入参，因为init属于构造的一部分，而成员方法test就不行；
    init {
        //可以调用入参变量
        Log.i("Test7", "name=${name} age=${age}")
    }

    private fun test() {
        //无法调用入参变量
        // Log.i("Test7", "name=${name} age=${age}")
    }
}

open class Test8Parent {
    var nameT: String? = null

    constructor(name: String) {
        nameT = name
    }
}

//继承Test8Parent
class Test8 : Test8Parent {

    var ageT: Int? = 0
    var sexT: Int? = 0

    //    5.2 次构造函数后面是调用this还是super，完全取决于该构造函数的功能，没有固定，但如果定义了主构造函数，那么次构造函数必须间接或者直接调用主构造函数；
    //冒号后面是调用this还是super，完全取决于该构造函数的功能，没有固定
    constructor(name: String, age: Int) : this(name, 0, 0) {
        ageT = age
    }

    //冒号后面是调用this还是super，完全取决于该构造函数的功能，没有固定
    constructor(name: String, age: Int, sex: Int) : super(name) {
        sexT = sex
    }
}

open class Test9Parent(name: String) {
    var nameT: String? = name
}

//继承Test9Parent
//5.2 如果父类有主构造函数，子类也必须得有，否则提示"Supertype initialization is impossible without primary constructor"
class Test9(name: String, age: Int, sex: Int) : Test9Parent(name) {
    var ageT: Int? = age
    var sexT: Int? = sex
}


//5.3 如果定义了主构造函数，可以把入参当作属性，省去定义；
class Test10 constructor(name: String, age: Int) {
    //常规定义属性的办法
    var nameT: String? = name
    var ageT: Int? = age
    private fun test() {
        Log.i("Test7", "name=${nameT} age=${ageT}")
    }
}

//把入参当作属性
class Test11 constructor(var nameT: String, var ageT: Int) {
    private fun test() {
        Log.i("Test7", "name=${nameT} age=${ageT}")
    }
}

//案例3，只有一个主构造函数，如果构造函数没有任何修改符（例如private）或注解，那么constructor关键字可以省略
class Test12(name: String?) {
    var name: String? = name
}


class Test {
    fun test4() {
        //只要有构造函数或者次构造函数里面有参数，那么就无法用用无参构造函数
//        var data: Test5 = Test5();
    }

}


