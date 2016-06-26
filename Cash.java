/**
 * Created by Matt on 6/26/2016.
 *
 * Represents a number of bills of different denominations
 */
public class Cash {

    private static final int[] denominationValues = { 20, 10, 5, 2, 1};

    static final int ONES_INDEX=4;

    int[] counts = {0,0,0,0,0};  // current number of bills per denomination, indexes correspond to denomination array values above

    public Cash(){}


    public Cash(int[] denominationCounts){
        for (int i=0; i< denominationCounts.length; i++) {
            counts[i] += denominationCounts[i];
            if (i==counts.length-1) break;
        }
    }

    /***
     * Assuming arguments are passed as the number of bills for denominations 20, 10, 5, 2, 1 separated by spaces
     * @param denominationAmounts
     * @return
     */
    public static Cash parse(String[] denominationAmounts){
        Cash result = new Cash();

        for (int index=0; index<denominationAmounts.length && index<result.counts.length; index++){
            try {
                int val = Integer.parseInt(denominationAmounts[index]);
                if (val<0) return null; // negative amounts invalid

                result.counts[index] = val;
            } catch (NumberFormatException e) {
                // invalid amount
                return null;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        int total = 0;
        StringBuilder result = new StringBuilder();
        for (int i=0; i< counts.length; i++) {
            total += counts[i] * denominationValues[i];

            result.append(" " ).append(counts[i]);
        }
        return "$" + total + result.toString();
    }

    public int getTotalAmount(){
        int total =0;
        for (int i=0; i< counts.length; i++)
            total += counts[i] * denominationValues[i];

        return total;
    }


    public void add(Cash value) {
        if (value!=null) {
            for (int i=0; i< counts.length; i++)
                counts[i] += value.counts[i];
        }
    }

    public boolean remove(Cash request) {

        // validate there is enough of each denomination to remove
        for (int i=0; i< counts.length; i++)
            if (counts[i]<request.counts[i]) return false;

        // amounts ok for all denominations, remove now
        for (int i=0; i< counts.length; i++) {
            counts[i] -= request.counts[i];
        }

        return true;
    }

    /***
     * Determine the number of bills of each denomination needed to make change for the given amount.
     * If a payout can be found, the combination is returned otherwise null is returned.
     *
     * @param amount  A set of values representing counts of bills of different denominations
     * @return
     */
    public Cash findPayoutForAmount(int amount) {
        if (amount<=0) return null;

        Cash cash = new Cash(counts);

        Cash combination = new Cash();
        int remaining = amount;
        for (int i=0; i< denominationValues.length; i++){
            while (cash.counts[i]>0 && remaining >= denominationValues[i]) {
                cash.counts[i] -=1;
                combination.counts[i]+=1;
                remaining -= denominationValues[i];
                if (remaining==0) return combination;

                // edge case - shouldn't hand out the last five
                // where no ones and there is an odd remainder to pay out, use twos instead
                if (denominationValues[i]==5 && cash.counts[ONES_INDEX]==0 && (remaining % 2 ==1) &&
                        combination.counts[i]>0 && cash.counts[i]==0   ) {
                    // put the last five back, use twos later for remainder
                    remaining += denominationValues[i];
                    cash.counts[i]+=1;
                    combination.counts[i] -=1;
                    break;
                }
            }
        }

        // still remaining to change after going through permutations, didn't work
        return null;
    }
}
