package com.ahimsa.models;

/**
 * @author Miguel Reyes
 *         Date: 11/17/14
 *         Time: 2:41 PM
 */
public class Order {
    public String _id;

    public long coffeeShopId;
    public DrinkType type;
    public String size;
    public String name;

    public class DrinkType {
        public String name;
        public String family;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DrinkType drinkType = (DrinkType) o;

            if (family != null ? !family.equals(drinkType.family) : drinkType.family != null) return false;
            if (name != null ? !name.equals(drinkType.name) : drinkType.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (family != null ? family.hashCode() : 0);
            return result;
        }
    }
}
