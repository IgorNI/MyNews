package com.materialdesign.myapplication.bean.city;

import java.util.List;

/**
 * @Description :
 * @Author : ni
 * @Email : lifengni2015@gmail.com
 * @Date : 2017/11/29
 */

public class CityInfo {
    /**
     * results : [{"address_components":[{"long_name":"426县道","short_name":"426县道","types":["route"]},{"long_name":"富阳区","short_name":"富阳区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"杭州市","short_name":"杭州市","types":["locality","political"]},{"long_name":"浙江省","short_name":"浙江省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国浙江省杭州市富阳区426县道","geometry":{"bounds":{"northeast":{"lat":30.1238289,"lng":119.9248606},"southwest":{"lat":30.1195909,"lng":119.9230965}},"location":{"lat":30.1216461,"lng":119.9237764},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":30.1238289,"lng":119.9253275302915},"southwest":{"lat":30.1195909,"lng":119.9226295697085}}},"place_id":"ChIJ1QS0syt2SzQRiqhkV_4Qtn0","types":["route"]},{"address_components":[{"long_name":"富阳区","short_name":"富阳区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"杭州市","short_name":"杭州市","types":["locality","political"]},{"long_name":"浙江省","short_name":"浙江省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国浙江省杭州市富阳区","geometry":{"bounds":{"northeast":{"lat":30.1969526,"lng":120.1468766},"southwest":{"lat":29.7444377,"lng":119.4363808}},"location":{"lat":30.048692,"lng":119.960076},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":30.1969526,"lng":120.1468766},"southwest":{"lat":29.7444377,"lng":119.4363808}}},"place_id":"ChIJpZ_vSfCXSzQRtQ6xYR0jF5U","types":["political","sublocality","sublocality_level_1"]},{"address_components":[{"long_name":"杭州市","short_name":"杭州市","types":["locality","political"]},{"long_name":"浙江省","short_name":"浙江省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国浙江省杭州市","geometry":{"bounds":{"northeast":{"lat":30.5665162,"lng":120.7219451},"southwest":{"lat":29.18875689999999,"lng":118.3449333}},"location":{"lat":30.274084,"lng":120.15507},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":30.4743515,"lng":120.4307556},"southwest":{"lat":30.0484295,"lng":119.9130249}}},"place_id":"ChIJmaqaQym2SzQROuhNgoPRv6c","types":["locality","political"]},{"address_components":[{"long_name":"浙江省","short_name":"浙江省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国浙江省","geometry":{"bounds":{"northeast":{"lat":31.1787819,"lng":122.9495085},"southwest":{"lat":27.0413557,"lng":118.0282788}},"location":{"lat":29.1416432,"lng":119.7889248},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":31.1787819,"lng":122.9495085},"southwest":{"lat":27.0413557,"lng":118.0282788}}},"place_id":"ChIJv8wKpTQSSTQRt3wYNsVx74E","types":["administrative_area_level_1","political"]},{"address_components":[{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国","geometry":{"bounds":{"northeast":{"lat":53.56097399999999,"lng":134.7728099},"southwest":{"lat":17.9996,"lng":73.4994136}},"location":{"lat":35.86166,"lng":104.195397},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":53.56097399999999,"lng":134.7728099},"southwest":{"lat":17.9996,"lng":73.4994136}}},"place_id":"ChIJwULG5WSOUDERbzafNHyqHZU","types":["country","political"]}]
     * status : OK
     */

    private String status;
    private List<ResultsBean> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * address_components : [{"long_name":"426县道","short_name":"426县道","types":["route"]},{"long_name":"富阳区","short_name":"富阳区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"杭州市","short_name":"杭州市","types":["locality","political"]},{"long_name":"浙江省","short_name":"浙江省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}]
         * formatted_address : 中国浙江省杭州市富阳区426县道
         * geometry : {"bounds":{"northeast":{"lat":30.1238289,"lng":119.9248606},"southwest":{"lat":30.1195909,"lng":119.9230965}},"location":{"lat":30.1216461,"lng":119.9237764},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":30.1238289,"lng":119.9253275302915},"southwest":{"lat":30.1195909,"lng":119.9226295697085}}}
         * place_id : ChIJ1QS0syt2SzQRiqhkV_4Qtn0
         * types : ["route"]
         */

        private String formatted_address;
        private GeometryBean geometry;
        private String place_id;
        private List<AddressComponentsBean> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<AddressComponentsBean> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsBean> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * bounds : {"northeast":{"lat":30.1238289,"lng":119.9248606},"southwest":{"lat":30.1195909,"lng":119.9230965}}
             * location : {"lat":30.1216461,"lng":119.9237764}
             * location_type : GEOMETRIC_CENTER
             * viewport : {"northeast":{"lat":30.1238289,"lng":119.9253275302915},"southwest":{"lat":30.1195909,"lng":119.9226295697085}}
             */

            private BoundsBean bounds;
            private LocationBean location;
            private String location_type;
            private ViewportBean viewport;

            public BoundsBean getBounds() {
                return bounds;
            }

            public void setBounds(BoundsBean bounds) {
                this.bounds = bounds;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportBean getViewport() {
                return viewport;
            }

            public void setViewport(ViewportBean viewport) {
                this.viewport = viewport;
            }

            public static class BoundsBean {
                /**
                 * northeast : {"lat":30.1238289,"lng":119.9248606}
                 * southwest : {"lat":30.1195909,"lng":119.9230965}
                 */

                private NortheastBean northeast;
                private SouthwestBean southwest;

                public NortheastBean getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBean northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBean getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBean southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBean {
                    /**
                     * lat : 30.1238289
                     * lng : 119.9248606
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBean {
                    /**
                     * lat : 30.1195909
                     * lng : 119.9230965
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }

            public static class LocationBean {
                /**
                 * lat : 30.1216461
                 * lng : 119.9237764
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportBean {
                /**
                 * northeast : {"lat":30.1238289,"lng":119.9253275302915}
                 * southwest : {"lat":30.1195909,"lng":119.9226295697085}
                 */

                private NortheastBeanX northeast;
                private SouthwestBeanX southwest;

                public NortheastBeanX getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBeanX northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBeanX getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBeanX southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBeanX {
                    /**
                     * lat : 30.1238289
                     * lng : 119.9253275302915
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBeanX {
                    /**
                     * lat : 30.1195909
                     * lng : 119.9226295697085
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class AddressComponentsBean {
            /**
             * long_name : 426县道
             * short_name : 426县道
             * types : ["route"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }

    /**
     * status : OK
     * result : {
     *  "location":{
     *      "lng":113.379763,
     *      "lat":23.131427
     *      },
     *  "formatted_address":"广东省广州市天河区科韵路16号-104",
     *  "business":"天园,天河公园,棠下",
     *  "addressComponent":{
     *      "city":"广州市",
     *      "direction":"西北",
     *      "distance":"57",
     *      "district":"天河区",
     *      "province":"广东省",
     *      "street":"科韵路",
     *      "street_number":"16号-104"
     *      },
     *   "cityCode":257
     *  }
     */

}
