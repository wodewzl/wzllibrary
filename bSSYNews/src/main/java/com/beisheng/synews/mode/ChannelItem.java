
package com.beisheng.synews.mode;

import java.io.Serializable;
import java.util.List;

public class ChannelItem implements Serializable {
    /**
     * code : 200
     * retinfo : 璇锋眰鎴愬姛锛�
     * list : [{"cid":"1","title":"澶存潯","fixed":"1","def":"1","sign":"0","type":"local"},{"cid":"2","title":"鏀挎儏","fixed":"1","def":"1","sign":"0","type":"government"},{"cid":"18","title":"绀句細","fixed":"1","def":"1","sign":"0","type":"shiyan"},{"cid":"9","title":"鍥剧墖","fixed":"1","def":"1","sign":"0","type":"image"},{"cid":"3","title":"鐑偣","fixed":"1","def":"1","sign":"0","type":"current"},{"cid":"12","title":"鎻","fixed":"1","def":"1","sign":"0","type":"decryption"},{"cid":"28","title":"鐩存挱","fixed":"1","def":"1","sign":"0","type":"liveplay"},{"cid":"5","title":"鍘垮競","fixed":"1","def":"1","sign":"0","type":"city"},{"cid":"6","title":"鍙戝竷","fixed":"0","def":"0","sign":"0","type":"sina"},{"cid":"13","title":"澶滆瘽","fixed":"0","def":"0","sign":"0","type":"mood"},{"cid":"16","title":"鏃呮父","fixed":"0","def":"0","sign":"1","type":"travel"},{"cid":"4","title":"濞变箰","fixed":"0","def":"0","sign":"0","type":"ent"},{"cid":"11","title":"鍛ㄨ竟","fixed":"0","def":"0","sign":"0","type":"periphery"},{"cid":"7","title":"鍗佸牥鏃ユ姤","fixed":"0","def":"0","sign":"1","type":"ribao"},{"cid":"8","title":"鍗佸牥鏅氭姤","fixed":"0","def":"0","sign":"1","type":"wanbao"}]
     * listv : [{"title":"鏂伴椈棰戦亾","list":[{"cid":"1","title":"澶存潯","fixed":"1","def":"1","sign":"0","type":"local"},{"cid":"2","title":"鏀挎儏","fixed":"1","def":"1","sign":"0","type":"government"},{"cid":"18","title":"绀句細","fixed":"1","def":"1","sign":"0","type":"shiyan"},{"cid":"9","title":"鍥剧墖","fixed":"1","def":"1","sign":"0","type":"image"},{"cid":"3","title":"鐑偣","fixed":"1","def":"1","sign":"0","type":"current"},{"cid":"12","title":"鎻","fixed":"1","def":"1","sign":"0","type":"decryption"},{"cid":"28","title":"鐩存挱","fixed":"1","def":"1","sign":"0","type":"liveplay"},{"cid":"5","title":"鍘垮競","fixed":"1","def":"1","sign":"0","type":"city"},{"cid":"6","title":"鍙戝竷","fixed":"0","def":"0","sign":"0","type":"sina"},{"cid":"13","title":"澶滆瘽","fixed":"0","def":"0","sign":"0","type":"mood"},{"cid":"16","title":"鏃呮父","fixed":"0","def":"0","sign":"1","type":"travel"},{"cid":"4","title":"濞变箰","fixed":"0","def":"0","sign":"0","type":"ent"},{"cid":"11","title":"鍛ㄨ竟","fixed":"0","def":"0","sign":"0","type":"periphery"},{"cid":"7","title":"鍗佸牥鏃ユ姤","fixed":"0","def":"0","sign":"1","type":"ribao"},{"cid":"8","title":"鍗佸牥鏅氭姤","fixed":"0","def":"0","sign":"1","type":"wanbao"}]},{"title":"鍘垮競鍖洪閬�","list":[{"cid":"888","title":"寮犳咕鍖�","fixed":"0","def":"1","sign":"1","type":"1","islink":"1","linkurl":"http://m.10yan.com"}]},{"title":"閮ㄩ棬棰戦亾","list":[{"cid":"999","title":"鐜繚灞�","fixed":"0","def":"1","sign":"1","type":"1","islink":"1","linkurl":"http://m.10yan.com"}]}]
     */

    private String code;
    private String retinfo;
    private List<ListBean> list;
    private List<ListvBean> listv;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRetinfo() {
        return retinfo;
    }

    public void setRetinfo(String retinfo) {
        this.retinfo = retinfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListvBean> getListv() {
        return listv;
    }

    public void setListv(List<ListvBean> listv) {
        this.listv = listv;
    }

    public static class ListBean implements Serializable{

        /**
         * cid : 1
         * title : 澶存潯
         * fixed : 1
         * def : 1
         * sign : 0
         * type : local
         * isdel: 1
         */

        private String cid;
        private String title;
        private String fixed;
        private String def;
        private String sign;
        private String type;
        private String isdel;



        public String getIsdel() {
            return isdel;
        }

        public void setIsdel(String isdel) {
            this.isdel = isdel;
        }



        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFixed() {
            return fixed;
        }

        public void setFixed(String fixed) {
            this.fixed = fixed;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ListvBean implements Serializable{
        /**
         * title : 鏂伴椈棰戦亾
         * list : [{"cid":"1","title":"澶存潯","fixed":"1","def":"1","sign":"0","type":"local"},{"cid":"2","title":"鏀挎儏","fixed":"1","def":"1","sign":"0","type":"government"},{"cid":"18","title":"绀句細","fixed":"1","def":"1","sign":"0","type":"shiyan"},{"cid":"9","title":"鍥剧墖","fixed":"1","def":"1","sign":"0","type":"image"},{"cid":"3","title":"鐑偣","fixed":"1","def":"1","sign":"0","type":"current"},{"cid":"12","title":"鎻","fixed":"1","def":"1","sign":"0","type":"decryption"},{"cid":"28","title":"鐩存挱","fixed":"1","def":"1","sign":"0","type":"liveplay"},{"cid":"5","title":"鍘垮競","fixed":"1","def":"1","sign":"0","type":"city"},{"cid":"6","title":"鍙戝竷","fixed":"0","def":"0","sign":"0","type":"sina"},{"cid":"13","title":"澶滆瘽","fixed":"0","def":"0","sign":"0","type":"mood"},{"cid":"16","title":"鏃呮父","fixed":"0","def":"0","sign":"1","type":"travel"},{"cid":"4","title":"濞变箰","fixed":"0","def":"0","sign":"0","type":"ent"},{"cid":"11","title":"鍛ㄨ竟","fixed":"0","def":"0","sign":"0","type":"periphery"},{"cid":"7","title":"鍗佸牥鏃ユ姤","fixed":"0","def":"0","sign":"1","type":"ribao"},{"cid":"8","title":"鍗佸牥鏅氭姤","fixed":"0","def":"0","sign":"1","type":"wanbao"}]
         */

        private String title;
        private List<ListBeanX> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        @Override
        public String toString() {
            return "ListvBean{" +
                    "list=" + list +
                    '}';
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public static class ListBeanX implements Serializable {
            public Integer id;
            public String name;
            public Integer orderId;
            public Integer selected;
            public ListBeanX(int id, String name, int orderId, int selected) {
                this.id = Integer.valueOf(id);
                this.name = name;
                this.orderId = Integer.valueOf(orderId);
                this.selected = Integer.valueOf(selected);
            }
            public int getId() {
                return this.id.intValue();
            }

            public String getName() {
                return this.name;
            }

            public int getOrderId() {
                return this.orderId.intValue();
            }

            public Integer getSelected() {
                return this.selected;
            }

            public void setId(int paramInt) {
                this.id = Integer.valueOf(paramInt);
            }

            public void setName(String paramString) {
                this.name = paramString;
            }

            public void setOrderId(int paramInt) {
                this.orderId = Integer.valueOf(paramInt);
            }

            public void setSelected(Integer paramInteger) {
                this.selected = paramInteger;
            }



            /**
             * cid : 1
             * title : 澶存潯
             * fixed : 1
             * def : 1
             * sign : 0
             * type : local
             * islink : 1
             * linkurl : http://m.10yan.com
             * isdel: 1/0
             * sorts: 1
             */

            private String cid;
            private String title;
            private String fixed;
            private String def;
            private String sign;
            private String type;
            private String islink;
            private String linkurl;
            private String isdel;
            private  String sorts;


            public String getSorts() {
                return sorts;
            }

            public void setSorts(String sorts) {
                this.sorts = sorts;
            }





            public String getIsdel() {
                return isdel;
            }

            public void setIsdel(String isdel) {
                this.isdel = isdel;
            }
            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getFixed() {
                return fixed;
            }

            public void setFixed(String fixed) {
                this.fixed = fixed;
            }

            public String getDef() {
                return def;
            }

            public void setDef(String def) {
                this.def = def;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
            public String getIslink() {
                return islink;
            }

            public void setIslink(String islink) {
                this.islink = islink;
            }


            public String getLinkurl() {
                return linkurl;
            }

            public void setLinkurl(String linkurl) {
                this.linkurl = linkurl;
            }

            @Override
            public String toString() {
                return "ListBeanX{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", orderId=" + orderId +
                        ", selected=" + selected +
                        ", cid='" + cid + '\'' +
                        ", title='" + title + '\'' +
                        ", fixed='" + fixed + '\'' +
                        ", def='" + def + '\'' +
                        ", sign='" + sign + '\'' +
                        ", type='" + type + '\'' +
                        ", islink='" + islink + '\'' +
                        ", linkurl='" + linkurl + '\'' +
                        '}';
            }
        }
    }

}
