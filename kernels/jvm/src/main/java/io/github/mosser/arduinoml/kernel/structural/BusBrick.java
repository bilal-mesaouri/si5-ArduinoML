package io.github.mosser.arduinoml.kernel.structural;

public abstract class BusBrick extends Brick {
    private int[] address;   // e.g., I2C address like "0x27"


    public String getAddressString() {
        StringBuilder addressString = new StringBuilder();
        for (int i = 0; i < address.length; i++) {
            addressString.append(address[i]);
            if (i < address.length - 1) {
                addressString.append(", ");
            }
        }
        return addressString.toString();
    }
    public int[] getAddress() {
        return address;
    }

    public void setAddress(int[]  address) {
        this.address = address;
    }

}
