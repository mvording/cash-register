import java.util.Scanner;

/**
 * Created by Matt on 6/26/2016.
 * Cash Register
 * Accept only $20, $10, $5, $2 and $1 bills. Given the charge and amount of money received return the
 change in each denomination that should be given from the cash register. Sometimes when the
 vendors couldn’t make exact change they would tell us they couldn’t make exact change.

Examples of commands
 > show
 $68 1 2 3 4 5

 > put 1 2 3 0 5
 $128 2 4 6 4 10

 > take 1 4 3 0 10
 $43 1 0 3 4 0

 > change 11
 0 0 1 3 0

 > show
 $32 1 0 2 1 0

 // show error if there is insufficient fund or no change can be made
 > change 14
 sorry

 // exit program
 > quit
 */
public class CashRegister {

    Cash contents = new Cash();  // current contents of the cash register .. a number of bills of different denominations

    /***
     * Assume input is from standard input
     * @param args
     */
    public static void main(String[] args) {

        CashRegister register = new CashRegister();

        Scanner scanIn = new Scanner(System.in);

        register.processRequests(scanIn);

        scanIn.close();

    }


    public CashRegister(){
        System.out.println("ready");
    }

    /***
     * Parse input one line at a time into commands
     * If commands are valid execute them
     * @param scanIn
     */
    public void processRequests(Scanner scanIn) {

        while (scanIn.hasNext()) {
            String input = scanIn.nextLine();

            if (input != null && input.trim().length() > 0) {

                String result ;

                String[] segments = input.split("\\s+");
                String[] arguments=null;
                if (segments.length>1) {
                    arguments = new String[segments.length - 1];
                    System.arraycopy(segments, 1, arguments, 0, arguments.length);
                }

                String command = segments[0].toLowerCase();


                if (command.equals("quit"))
                    break;

                else if (command.equals("show"))
                    result = show();

                else if (command.equals("put")) {
                    result = put( Cash.parse(arguments) );

                } else if (command.equals("take")) {
                    result = take( Cash.parse(arguments) );

                } else if (command.equals("change")) {
                    int changeAmount = 0;

                    if (arguments.length == 1)
                        try {
                            changeAmount = Integer.parseInt(arguments[0]);
                        } catch (NumberFormatException e) {
                            result = "invalid";
                        }
                    else
                        result = "invalid";

                    result = change(changeAmount);

                } else
                    result = "unknown command";

                System.out.println(result);

            }
        }
    }



    private String show(){
        return contents.toString();
    }

    /***
     * Add bills to the cash register
     * @param request
     * @return
     */
    private String put(Cash request){
        if (request==null) return "invalid";

        contents.add(request);
        return contents.toString();
    }

    /****
     * Take bills from the cash register
     * @param request
     * @return
     */
    private String take(Cash request){
        if (request==null) return "invalid";

        if (contents.remove(request))
            return contents.toString();
        else
            return "sorry";
    }

    /***
     * Attempt to make change for the given amount from the cash register
     * If possible, update the state to reflect bills removed
     * @param amount
     * @return
     */
    private String change(int amount) {
        if (amount<=0) return "invalid";
        if (contents.getTotalAmount()< amount) return "sorry";

        Cash combination = contents.findPayoutForAmount(amount);

        if (combination!=null) {
            contents.remove(combination);
            return combination.toString();
        }
        else
            return "sorry";

    }


}
