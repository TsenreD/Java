package game;

import java.util.Arrays;

public class IntList {

    private int[] nums;
    private int pointer;

    public IntList() {
        this.nums = new int[2];
        this.pointer = 0;
    }

    public IntList(int[] arr) {
        this.nums = Arrays.copyOf(arr, arr.length);
        this.pointer = arr.length;
    }

    public int size() {
        return pointer;
    }

    public void checkPos(int pos) {
        if (pos >= pointer || pos < 0) {
            throw new IndexOutOfBoundsException("IntList index out of bounds");
        }
    }

    public int get(int pos) {
        checkPos(pos);
        return nums[pos];
    }

    public void set(int pos, int value) {
        checkPos(pos);
        nums[pos] = value;
    }
    public boolean containsNonPositive() {
        for (int i = 0; i < pointer; i++) {
            if (nums[i] <= 0) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        this.nums = new int[2];
        this.pointer = 0;
    }

    public void add(int value) {
        if (pointer >= nums.length) {
            nums = Arrays.copyOf(nums, nums.length * 2);
        }
        nums[pointer] = value;
        pointer++;
    }

    @Override
    public String toString() {
        return Arrays.toString(nums);
    }

}
