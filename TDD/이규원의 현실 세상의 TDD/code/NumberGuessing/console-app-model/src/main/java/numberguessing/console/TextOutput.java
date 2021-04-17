package numberguessing.console;

public class TextOutput {

    private final StringBuffer outputBuffer;

    public TextOutput(String selectModeMessage) {
        outputBuffer = new StringBuffer(selectModeMessage);
    }

    public String flushOutput() {
        String output = outputBuffer.toString();
        outputBuffer.setLength(0);
        return output;
    }

    public void printLines(String... lines) {
        outputBuffer.append(String.join(System.lineSeparator(), lines));
    }

}
