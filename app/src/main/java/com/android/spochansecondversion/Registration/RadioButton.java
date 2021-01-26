package com.android.spochansecondversion.Registration;

public class RadioButton {

    private android.widget.RadioButton button;
    private int counter;

    public RadioButton(android.widget.RadioButton button, int counter) {
        this.button = button;
        this.counter = counter;
    }

    public android.widget.RadioButton getButton() {
        return button;
    }

    public void setButton(android.widget.RadioButton button) {
        this.button = button;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
