package com.bytedance.jstu.chapter5.api;

import java.util.List;

public class Youdao {
    private Meta meta;
    public class Meta {
        private List<String> dicts;

        public List<String> getDicts() {
            return dicts;
        }

        public void setDicts(List<String> dicts) {
            this.dicts = dicts;
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
