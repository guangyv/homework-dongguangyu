package com.bytedance.jstu.chapter5.api;

import java.util.List;

public class YoudaoRes {
    private WebTrans webTrans;
    public class WebTrans {
        private List<WebTranslation> webTranslation;
        public class WebTranslation {
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public List<WebTranslation> getWebTranslation() {
            return webTranslation;
        }

        public void setWebTranslation(List<WebTranslation> webTranslation) {
            this.webTranslation = webTranslation;
        }
    }

    public WebTrans getWebTrans() {
        return webTrans;
    }

    public void setWebTrans(WebTrans webTrans) {
        this.webTrans = webTrans;
    }
}
