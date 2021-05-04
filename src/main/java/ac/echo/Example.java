package ac.echo;

public class Example {
    public static void main(String[] args) {

        API api = new API();

        String key = "x";

        String pin = api.getCurrentPin(key);

        System.out.println((new Key(key, api)).getUsername());

        System.out.println(pin);


        api.registerProgressListener(pin, new ProgressListener() {
            public void onProgressChange(int newProgress) {
                System.out.println("new progress: " + newProgress);
            }

            public void onScanFinish() {
                System.out.println("finished!");

                Scan scan = api.getMostRecentScan(key);

                System.out.println(String.join(",", scan.getAlts()));
            }

            public void onScanStart() {
                System.out.println("started");
            }

            public void onBeginListening() {
                System.out.println("began listening for scan");

            }
        });
    }
}
