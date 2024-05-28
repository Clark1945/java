package org.example.design_pattern.behavior.singleton;

//懶散模式(Lazy)
public class BlueDragon {
    private static BlueDragon instance;

    //私有的建構式讓別人不能創造
    private BlueDragon() {}

    //因為整個系統都要存取這個類別，很可能有多個process或thread同時存取
    //為了讓線程安全添加synchronized在多線程下確保物件唯一性
    //但是這個實現方式每次都需要進行同步，效率會很很低。
    public static synchronized BlueDragon getInstance() {
        if (instance == null) {
            instance = new BlueDragon();
        }
        return instance;
    }

    // 而餓漢，因為在Class初始化就給予值了(並且加上final前綴字保證不被修改)，完全可以避免Concurrency，但是如果物件沒有使用到的話就會白白浪費記憶體先創立一個此Singleton物件
    // 懶漢與餓漢差別
    //在於物件的初始化實機，懶就是晚一點餓就是一開始就來。
}
