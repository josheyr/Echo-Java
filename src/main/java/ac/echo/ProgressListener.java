package ac.echo;

public interface ProgressListener {
    void onProgressChange(int newProgress);

    void onScanFinish();

    void onScanStart();

    void onBeginListening();
}
