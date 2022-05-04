package com.bytedance.jstu.chapter5.api;

public class YoudaoSentence {
    private Fanyi fanyi;
    public class Fanyi {
        private String input;
        private String type;
        private String tran;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTran() {
            return tran;
        }

        public void setTran(String tran) {
            this.tran = tran;
        }
    }

    public Fanyi getFanyi() {
        return fanyi;
    }

    public void setFanyi(Fanyi fanyi) {
        this.fanyi = fanyi;
    }
}
