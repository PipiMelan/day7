package 反射

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/*
* Reflection and annotation
* 在程序运行过程中动态地调用类的属性和方法
* Properties and methods of a class are called dynamically during program execution
* Bytecode file :
* kotlin: KClass
* Java:   Class
* 1.How to get the bytecode file  type
*    //a. class name only
     val clz = Person::class
     //b. certain object only
     val xw = Person()
     val clz2 = xw.javaClass.kotlin
* */

fun classInfo(){
    //a. class name only
    val clz = Person::class
    //b. certain object only
    val xw = Person("")
    val clz2 = xw.javaClass.kotlin

    // Get the detailed information of class through class object
    println(clz.simpleName)     //class name
    println(clz.qualifiedName)  // full name of the class
//    println(clz.supertypes)     //super class java -> Object ; Kotlin -> Any

    // get the declared class attributes in super class and itself
//    clz.declaredMemberProperties.forEach{ println(it)}
    // get the declared attributes by itself
//    clz.memberProperties.forEach{ println(it)}

    println()
    // Get the main constructor
//    clz.primaryConstructor.also{ println(it)}

    //Get all teh constructors
    clz.constructors.forEach{ println(it)}


    println()
    //Get other functions
//    clz.memberFunctions.forEach.also{ println(it)}
    //Get declared functions by itself
//    clz.declaredmemberFunctions.forEach.also{ println(it)}
}


/*
* KFunction
* KCallable
* KParameter
* KProperty1
* KMutableProperty
* */


// Reflect top-level method ::test
fun test(){
    println("Test")
}

fun main() {
/*//    val person = Person()
    //get the KClass object
    // as Person -> Transfer 'Any' to 'Person'
    //Person.KClass -> Any -> Person
//    val xw = createObj(Person::class) as Person
//    println(xw.name)*/

//    invokeFun(Person::class,"show")
//    invokeProperty(Person::class,"age")

    val func = ::test
    func.call()
}
// invoke attributes
fun invokeProperty(clz:KClass<out Any>,propertyName:String){
    /*
   * Person().name = ""
   * Person().age = 20
   * */
    val primary = clz.primaryConstructor
    val obj = primary?.call("Android Master")

    //
    clz.memberProperties.find { it.name == propertyName }.also {
       /* //invoke the get method in object
        val value = it?.call(obj)
        println("Get the value of ${propertyName}:${it?.call(obj)}")*/

        //The 'set' method in object
        //transfer the type of KProperty into KMutableProperty
        //setValue(value:String):Unit  KMutableProperty1 -> Only one parameter in function
        //KMutableProperty1<T,R>

        // We must set isAccessible as true if the methods or attributes is modified by "private"
    if(it != null){
        it.isAccessible = true

        val kImpl = it as KMutableProperty1<Any,Any>
        //obj.setName("jack")
        kImpl.set(obj!!,100)

        val result = it.call(obj
        )
        println(result)
    }

    }
    val p = obj as Person
    println(p.age)
}

// functions invocation
fun invokeFun(clz:KClass<out Any>,funName:String){
    //Person().show("ss")
    // get the main constructors by default -> Don't its parameters
    val priCon = clz.primaryConstructor
    //Create object
    val obj = priCon?.call("MZD")

    // find the function
    for(func in clz.functions){
        if(func.name == funName){
            //Pass the object when invoking the methods in class
            // Get the numbers of parameters
            func.parameters.forEach{
//                println("第${it.index}个参数")
//                println("The type is ${it.type}")
            }
            func.call(obj,"ZEL")
            break
        }
    }
//    clz.functions.find {it.name == funName}.also { println(it) }
}

fun createObj(klz: KClass<out Any>): Any{
    //1. Use the constructors without parameters by default
   // return klz.createInstance()
    //2. Create object through constructor with parameters

    //    println("function name:${priConstr?.name}")
//    println("function return type:${priConstr?.returnType}")
//    println("function parameters:${priConstr?.parameters}")
    val priConstr= klz.primaryConstructor

    //invoke the constructors
    val obj = priConstr?.call("mmm")
    return obj!!
}

open class Father():Any(){
    var address : String = "xndx"

    fun test(){

    }
}

class Person(var name:String): Father() {
    var age :Int = 20

    constructor():this("小王"){    }

    fun show(des:String){
        println("des: $des")
        println("my name is $name")
    }
}