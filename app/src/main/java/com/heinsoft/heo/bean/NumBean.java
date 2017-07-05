package com.heinsoft.heo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24/024.
 */
public class NumBean implements Serializable {

    private List<OutputsBean> outputs;

    public List<OutputsBean> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputsBean> outputs) {
        this.outputs = outputs;
    }

    public static class OutputsBean {

        private String outputLabel;
        private OutputMultiBean outputMulti;
        private OutputValueBean outputValue;

        public String getOutputLabel() {
            return outputLabel;
        }

        public void setOutputLabel(String outputLabel) {
            this.outputLabel = outputLabel;
        }

        public OutputMultiBean getOutputMulti() {
            return outputMulti;
        }

        public void setOutputMulti(OutputMultiBean outputMulti) {
            this.outputMulti = outputMulti;
        }

        public OutputValueBean getOutputValue() {
            return outputValue;
        }

        public void setOutputValue(OutputValueBean outputValue) {
            this.outputValue = outputValue;
        }

        public static class OutputMultiBean {
        }

        public static class OutputValueBean {


            private int dataType;
            private String dataValue;

            public int getDataType() {
                return dataType;
            }

            public void setDataType(int dataType) {
                this.dataType = dataType;
            }

            public String getDataValue() {
                return dataValue;
            }

            public void setDataValue(String dataValue) {
                this.dataValue = dataValue;
            }
        }
    }
}