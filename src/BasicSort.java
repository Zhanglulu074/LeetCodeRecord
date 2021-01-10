import java.util.Arrays;

public class BasicSort {

    public void insertSort(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        for (int i = 1; i < nums.length; i++) {
            int tmp = nums[i];
            int j = i - 1;
            while (j >= 0 && nums[j] >= tmp) {
                nums[j + 1] = nums[j];
                j--;
            }
            nums[j + 1] = tmp;
        }
    }

    public void selectSort(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            int min = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] <= nums[min]) {
                    min = j;
                }
            }
            int tmp = nums[min];
            nums[min] = nums[i];
            nums[i] = tmp;
        }
    }

    public void bubbleSort(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        boolean sorted = false;
        for (int i = nums.length - 1; i > 0 && !sorted ; i--) {
            sorted = true;
            for (int j = 0; j < i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                    sorted = false;
                }
            }
        }
    }

    public int[] mergeSort(int[] nums) {
        if (nums.length > 1) {
            int mid = (nums.length - 1) / 2;
            int[] left = Arrays.copyOfRange(nums, 0, mid + 1);
            int[] right = Arrays.copyOfRange(nums, mid + 1, nums.length);
            left = mergeSort(left);
            right =mergeSort(right);
            return merged(left,right);
        } else {
            return nums;
        }
    }

    public int[] merged(int[] a, int[] b) {
        int[] res = new int[a.length + b.length];
        int i = 0, j = 0, cur = 0;
        while (i < a.length && j < b.length){
            if (a[i] <= b[j]) {
                res[cur++] = a[i++];
            } else {
                res[cur++] = b[j++];
            }
        }
        if (i == a.length) {
            for (int k = j; k < b.length; k++) {
                res[cur++] = b[j++];
            }
        }
        if (j == b.length) {
            for (int k = i; k < a.length; k++) {
                res[cur++] = a[i++];
            }
        }
        return res;
    }

    public void quick(int[] nums, int p, int q) {
        if (p >= q) {
            return;
        }
        int tail = nums[q];
        int dummy = p - 1;
        for (int i = p; i < q; i++) {
            if (nums[i] <= tail) {
                dummy++;
                swap(nums, dummy, i);
            }
        }
        swap(nums, dummy + 1, q);
        quick(nums, p, dummy);
        quick(nums,dummy + 2, q);
    }

    public void swap(int[] nums, int p, int q) {
        int tmp = nums[p];
        nums[p] = nums[q];
        nums[q] = tmp;
    }

    //第K大的数字
    public int Ksort(int[] nums, int k) {
        if (k >= nums.length || k <= 0 || nums == null || nums.length == 0) {
            return -1;
        }
        return KsortNumber(nums, 0, nums.length - 1, k);
    }

    public int KsortNumber(int[] nums, int start, int end, int k) {
        int p = KsortNumberHelper(nums, start, end);
        if (p == end + 1 - k) {
            return nums[p];
        } else if (p < end + 1 - k) {
            return KsortNumber(nums, p + 1, end, k);
        } else {
            return KsortNumber(nums, start, p - 1,  p - end - 1 + k);
        }
    }

    public int KsortNumberHelper(int[] nums, int start, int end) {
        int tmp = nums[end];
        int p = start - 1;
        for (int i = p + 1; i < end; i++) {
            if (nums[i] < tmp) {
                swap(nums, i, ++p);
            }
        }
        swap(nums, end, p + 1);
        return p + 1;
    }

    public static void main(String[] args) {
        int[] test1 = new int[]{1, 7, 3, 4, 7, 9, 4, 10, 11};
        BasicSort sort = new BasicSort();
        int res = sort.Ksort(test1, 8);
        System.out.println(res);
        int x = 1;
    }
}
