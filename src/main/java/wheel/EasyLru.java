package wheel;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.util.Map;

public class EasyLru {

    private static class User {
        public Long id;
        public String name;
    }
    public static void main(String[] args) {
        Map<Long, User> easyLru = new ConcurrentLinkedHashMap
                .Builder<Long, User>()
                .maximumWeightedCapacity(100)
                .weigher(Weighers.singleton()).build();
    }
}
