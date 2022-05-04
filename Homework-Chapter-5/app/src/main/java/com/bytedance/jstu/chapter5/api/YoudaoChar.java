package com.bytedance.jstu.chapter5.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YoudaoChar {

    private String input;
    private Blng blng_sents_part;
    public class Blng {
        @SerializedName("trs-classify")
        private List<Trs> trs;
        public class Trs {
            private String proportion;
            private String tr;

            public String getProportion() {
                return proportion;
            }

            public void setProportion(String proportion) {
                this.proportion = proportion;
            }

            public String getTr() {
                return tr;
            }

            public void setTr(String tr) {
                this.tr = tr;
            }
        }

        public List<Trs> getTrs() {
            return trs;
        }

        public void setTrs(List<Trs> trs) {
            this.trs = trs;
        }
    }
//    private Ce ce;

//    public class Ce {
//        private List<Word> word;
//        public class Word {
//            private List<Trs> trs;
//            public class Trs {
//                private List<Tr> tr;
//                public class Tr {
//                    private L l;
//                    public class L {
//                        private String pos;
//                        private List<I> i;
//                        public class I {
//                            @SerializedName("#text")
//                            private String text;
//                            @SerializedName("@action")
//                            private String action;
//                            @SerializedName("@href")
//                            private String href;
//
//                            public String getText() {
//                                return text;
//                            }
//
//                            public void setText(String text) {
//                                this.text = text;
//                            }
//
//                            public String getAction() {
//                                return action;
//                            }
//
//                            public void setAction(String action) {
//                                this.action = action;
//                            }
//
//                            public String getHref() {
//                                return href;
//                            }
//
//                            public void setHref(String href) {
//                                this.href = href;
//                            }
//                        }
//
//                        public String getPos() {
//                            return pos;
//                        }
//
//                        public void setPos(String pos) {
//                            this.pos = pos;
//                        }
//
//                        public List<I> getI() {
//                            return i;
//                        }
//
//                        public void setI(List<I> i) {
//                            this.i = i;
//                        }
//                    }
//
//                    public L getL() {
//                        return l;
//                    }
//
//                    public void setL(L l) {
//                        this.l = l;
//                    }
//                }
//
//                public List<Tr> getTr() {
//                    return tr;
//                }
//
//                public void setTr(List<Tr> tr) {
//                    this.tr = tr;
//                }
//            }
//            public List<Trs> getTrs() {
//                return trs;
//            }
//
//            public void setTrs(List<Trs> trs) {
//                this.trs = trs;
//            }
//        }
//        public List<Word> getWord() {
//            return word;
//        }
//
//        public void setWord(List<Word> word) {
//            this.word = word;
//        }
//
//    }

    public Blng getBlng_sents_part() {
        return blng_sents_part;
    }

    public void setBlng_sents_part(Blng blng_sents_part) {
        this.blng_sents_part = blng_sents_part;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput(){
        return input;
    }

}
