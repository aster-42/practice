package thread;

/**
 * init when used for singleton
 * */
public class SingletonLazyInit {

    /**
     * dcl : double check locking
     * */
    public static class SingletonDcl {
        private volatile static SingletonDcl uniqueInstance;
        private SingletonDcl() { }
        public static SingletonDcl getInstance() {
            if(uniqueInstance == null) {
                synchronized(SingletonDcl.class) {
                    if(uniqueInstance == null){
                        uniqueInstance = new SingletonDcl();
                    }
                }
            }
            return uniqueInstance;
        }
    }

    /**
     * lazy initialization holder class idiom
     * */
    public static class LazyInit {
        private LazyInit() { }
        private static class SingleHolder {
            private static final LazyInit ins = new LazyInit();
        }
        public static LazyInit getInstance(){
            return SingleHolder.ins;
        }
    }

    public static void main(String[] args) {
        SingletonDcl dcl = SingletonDcl.getInstance();
        LazyInit lazyInit = LazyInit.getInstance();
    }
}
