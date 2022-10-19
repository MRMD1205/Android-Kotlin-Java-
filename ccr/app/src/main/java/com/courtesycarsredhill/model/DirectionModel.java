package com.courtesycarsredhill.model;

import java.util.List;

public class DirectionModel {

    private String status;
    private List<GeocodedWaypointsBean> geocoded_waypoints;
    private List<RoutesBean> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodedWaypointsBean> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(List<GeocodedWaypointsBean> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public List<RoutesBean> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RoutesBean> routes) {
        this.routes = routes;
    }

    public static class GeocodedWaypointsBean {

        private String geocoder_status;
        private String place_id;
        private List<String> types;

        public String getGeocoder_status() {
            return geocoder_status;
        }

        public void setGeocoder_status(String geocoder_status) {
            this.geocoder_status = geocoder_status;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }

    public static class RoutesBean {

        private BoundsBean bounds;
        private String copyrights;
        private OverviewPolylineBean overview_polyline;
        private String summary;
        private List<LegsBean> legs;
        private List<?> warnings;
        private List<?> waypoint_order;

        public BoundsBean getBounds() {
            return bounds;
        }

        public void setBounds(BoundsBean bounds) {
            this.bounds = bounds;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public OverviewPolylineBean getOverview_polyline() {
            return overview_polyline;
        }

        public void setOverview_polyline(OverviewPolylineBean overview_polyline) {
            this.overview_polyline = overview_polyline;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<LegsBean> getLegs() {
            return legs;
        }

        public void setLegs(List<LegsBean> legs) {
            this.legs = legs;
        }

        public List<?> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<?> warnings) {
            this.warnings = warnings;
        }

        public List<?> getWaypoint_order() {
            return waypoint_order;
        }

        public void setWaypoint_order(List<?> waypoint_order) {
            this.waypoint_order = waypoint_order;
        }

        public static class BoundsBean {
            /**
             * northeast : {"lat":52.9550502,"lng":-1.1865708}
             * southwest : {"lat":52.5990776,"lng":-1.3107306}
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
                 * lat : 52.9550502
                 * lng : -1.1865708
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
                 * lat : 52.5990776
                 * lng : -1.3107306
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

        public static class OverviewPolylineBean {
            /**
             * points : img`IvpiF`@|@VX^Jj@M`@c@zAsGj@MPFx@^l@wEjA}JP{CJ_U?kHO_CaAqBG_@Dq@XSh@D~@?|B[lEuA`A{@N_@d@Up@LrAG`DoA|CkA|D{@pCOnDR|DhAzNxGjANt@SRGhAe@`BuAnBgBhAo@fB[pADr@NtDdB`EpBdG`CdCd@rCTvBJrFQxT{@fHiArLwD|D}@tEm@pB]TIx@_Al@c@fAZt@B~Ba@~Ba@~@?dA?NBRNHdACh@Mx@q@`Bq@p@sAp@k@hAQv@MrKAjKFjAJ^n@z@h@`BH|CKhAy@xAq@RgCmAuGXcCVeDXs@Ls@IsCz@aFfB_LfFsMbIyT~N{s@fe@w{@hk@kNjJqFtE{WdXgQ|QaInJ{DhF{KlPoIvNcI`PeIbRqGxPyD`LaFlQyFvT}EnTcCtKqDlNmHlXaFjPkF|OiH`S}HxReG`NwGhMsIlMiGlH_HvGmFdEgL`HwGvCqLdDeLzA_ETcIL_Kh@{HhAoKvCeE|AeGhB_FdAsEn@iGd@qLFif@iBgLc@kIc@eG_AyEmAuDsAuE{B}IkGqJ}IyGyF_KqGkFaCyIsCuI{A_Km@mFBgGZ_LZwKYyFm@oJoB{GyB_KwEcSsJ}O}FeIaCeKaCsJeBuLwAgQgAgHSaHEgGL}Pz@eIx@_Gx@kJ`B}EdA}N`EmJjDoKlEgRfJ_OlIeNvIgPjL{IdHcQfOsJ`JiIpIqArAcW~YeGnHqGrGiI~GoFlDoDnBiH`DyDpAqFvAoE|@oI|@aCNyEFmJO_J{@eJgBwJaDuv@k\w\mNyK_DsGqAwEo@yD_@cG]eMIwKf@sFl@mGbAkJxBoIrCwGnC}S~IwThJym@nWs`@rP_IfDkDhA}D~@gDb@kDTmIEgH{@}GeBuEkBgGiDgIoGyIcJsGkHiE_FqDqDcF}DaEcCaEkBiE}AqDy@qEm@qDW}DEqId@iI~AgGlBsGxAeEl@_IX}FWmDa@{Es@KNMBoCG{D?qHRa@P]`@Up@UbB}@`A_@D_@Ii@g@Ue@cBeIg@cDByDiAqIqAsIWgC}BkLiAqHcAwKm@mQ}@sb@}BscAUqMWmA_@{@AS?Qa@gAy@aAuCoDkF_GuG{FeJoG{JkHiCcCkFyF_O_R_HsGkFeEiCyAa@IEXYd@YPQCETYt@g@tDeAlCoDbGeFhI{FfMwAdE@HAHIJMMMc@mFyFcDgDqE{FwBsCoC_D_MgMwJ{JyD_E{BiBaGsC{JaDeCaAc@UDqFJaHOeLhD]I{F
             */

            private String points;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }
        }

        public static class LegsBean {
            /**
             * distance : {"text":"52.1 km","value":52081}
             * duration : {"text":"44 mins","value":2615}
             * end_address : 8 Plantation Rd, Nottingham NG8 2ER, UK
             * end_location : {"lat":52.9542406,"lng":-1.2401269}
             * start_address : 175 Ryder Rd, Leicester LE3 6UZ, UK
             * start_location : {"lat":52.6358854,"lng":-1.2009239}
             * steps : [{"distance":{"text":"0.3 km","value":287},"duration":{"text":"1 min","value":61},"end_location":{"lat":52.6339879,"lng":-1.1999181},"html_instructions":"Head <b>southwest<\/b> on <b>Ryder Rd<\/b> toward <b>Cherry Hills Rd<\/b>","polyline":{"points":"img`IvpiFHTFNFLFHJJJLPHL@FAH?HANIFCFGHKFKFOJ[DYP}@Jq@Nw@FQJSNKFAH?H?HBFBx@^"},"start_location":{"lat":52.6358854,"lng":-1.2009239},"travel_mode":"DRIVING"},{"distance":{"text":"0.7 km","value":692},"duration":{"text":"1 min","value":77},"end_location":{"lat":52.6335555,"lng":-1.190027},"html_instructions":"Turn <b>left<\/b> onto <b>Scudamore Rd<\/b>","maneuver":"turn-left","polyline":{"points":"mag`InjiFl@wE~@kHJqAHqAFiAFqB?Y@wA@{M@kEA_BAwAMg@KWe@cA"},"start_location":{"lat":52.6339879,"lng":-1.1999181},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":345},"duration":{"text":"1 min","value":44},"end_location":{"lat":52.6310098,"lng":-1.1886238},"html_instructions":"At the roundabout, take the <b>3rd<\/b> exit onto <b>New Parks Way<\/b>/<b>A563<\/b>","maneuver":"roundabout-left","polyline":{"points":"w~f`ItlgFGGGMCIAEAG?GAO@K@KBIFGFEBCDAF?J?FDL?T@H?RAJ?j@GZEVC\\IREp@Oz@W`@QNGRIDC\\S`@]"},"start_location":{"lat":52.6335555,"lng":-1.190027},"travel_mode":"DRIVING"},{"distance":{"text":"3.3 km","value":3314},"duration":{"text":"3 mins","value":186},"end_location":{"lat":52.6021441,"lng":-1.1870107},"html_instructions":"At the roundabout, take the <b>2nd<\/b> exit onto <b>Braunstone Way<\/b>/<b>A563<\/b><div style=\"font-size:0.9em\">Continue to follow A563<\/div>","maneuver":"roundabout-left","polyline":{"points":"ynf`IzcgF?A?A?A@A?A?A?AFOFKDGFCDCPEJFH@N@J@T@NAXGR?VKTKTIJCVIb@SHEJEh@S^O@ATILGHCPGPGRId@MLCTG\\INCTEZCJCPAZCXCX?D?F?TAJ?V@^@L?TBh@DH@RBTDTDNDd@LRFVH\\LPHNFvIbEf@Vb@RZLLDNFLDPBF@TBZD^ATQPE@ALEPGVIPMVQRO\\[VWVUNOj@i@ZWHGPKZQPI^KZGRCVCT?P?N@XBD@D?XHLBNF^PhAh@z@`@LHNFBBJD\\Pp@Zf@VZN`@RVLn@Xj@TLFXH^Lf@N`@HRDl@L`@FZDPB^DdADjAHL?H@R?^A`@Ah@CfCInBIjCK~@E~AEzDMH?t@E`@Ed@E`@Ev@KFAXGv@M|@UNCz@UNGp@Sh@QnAa@pAc@d@ObA]f@Oz@Sp@Mf@Kj@G~ASd@Gb@I^GLAt@MLEDANGt@o@"},"start_location":{"lat":52.6310098,"lng":-1.1886238},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":271},"duration":{"text":"1 min","value":24},"end_location":{"lat":52.5998008,"lng":-1.1865206},"html_instructions":"At the roundabout, take the <b>2nd<\/b> exit onto <b>Lubbesthorpe Way<\/b>/<b>A563<\/b>","maneuver":"roundabout-left","polyline":{"points":"kz``IxyfFBOFKNOHCJCJ@LFHJb@DJ@J@L?N?NAl@MVEh@KnAUn@Kb@I"},"start_location":{"lat":52.6021441,"lng":-1.1870107},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":320},"duration":{"text":"1 min","value":48},"end_location":{"lat":52.60040490000001,"lng":-1.1891635},"html_instructions":"Slight <b>right<\/b> toward <b>A5460<\/b>","maneuver":"turn-slight-right","polyline":{"points":"wk``IvvfFZHJ?P?R?R?NBRNHdA?VCPE^GXK\\M\\IPMRKNIJIHQJMFg@RQJKHONEHINK^Qv@"},"start_location":{"lat":52.5998008,"lng":-1.1865206},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":328},"duration":{"text":"1 min","value":35},"end_location":{"lat":52.60025,"lng":-1.1938903},"html_instructions":"Merge onto <b>A5460<\/b>","maneuver":"merge","polyline":{"points":"oo``IfggFMrKCfJ@b@?X@R@LBNDNDNXd@"},"start_location":{"lat":52.60040490000001,"lng":-1.1891635},"travel_mode":"DRIVING"},{"distance":{"text":"0.7 km","value":670},"duration":{"text":"1 min","value":54},"end_location":{"lat":52.6045644,"lng":-1.196207},"html_instructions":"At the roundabout, take the <b>3rd<\/b> exit onto the <b>M1<\/b> ramp to <b>Nottingham<\/b>/<b>Leicester (N)<\/b>/<b>Newark<\/b>","maneuver":"roundabout-left","polyline":{"points":"qn``IxdhFTTLRHRHXF^Dj@@^@b@?l@A`@CLAFCPGVA@KRc@j@KHODMBG?G?GCGCYUa@UWMKEOEQACAI?I@_AD{BNY@UBw@D]BUBWHc@Ba@Ds@Fk@Fs@Ls@I"},"start_location":{"lat":52.60025,"lng":-1.1938903},"travel_mode":"DRIVING"},{"distance":{"text":"37.6 km","value":37641},"duration":{"text":"24 mins","value":1448},"end_location":{"lat":52.9112691,"lng":-1.3000435},"html_instructions":"Merge onto <b>M1<\/b>","maneuver":"merge","polyline":{"points":"oia`IhshF{Bp@WHYHeC|@WJi@RcBr@}BbAa@R{CzAeB`AeCzAsAz@sChBqCfBuA`AwCnB}@l@iAr@{@l@g@\\mAv@gAr@[R_@Ti@^y@f@m@^gAr@sBrAyHbFqCjBuBtAwBtAuBtAcC|A}AdAwDbCw@f@_@TgBlAaC~AoD`CgBjAuCnBiC`BcC|Ag@\\o@`@o@b@gLrHqH|Eg@\\OJgIjFk@^u@f@aAv@cAv@k@d@k@f@uAnAgDbDoFrFgInI{@|@yC|CcCdCsDxDuB~B_@b@yA`BmAvAyBpCcAtAeAtAW^Y\\_ArAaChDkBrCmBzCcBpCg@z@gBxC{AnCkCbFoB|Dy@dBMXq@vAkAfCcB|DcBdEmB|E_AfCMXuAxDi@xAi@|A]bAgAdD}@rC}@|Cu@lCo@lC}@hD{@bD{@`DcAfEu@bDy@nDy@vDs@bD_AdEcAnE}AjGsA`FoA|EqA`FeAtDeAvD_A~CcAhDw@dCe@zAEJu@bC_B|EoAnDi@zAc@nA_CnG{AdEgBrEYr@qB`FiAnCa@~@]v@gB~D}AhDoAhCq@tAiBdDk@bAaBlCkAjByAtBkA|AaApAoAzAeAjAq@r@{CzCs@p@o@j@_@\\_@\\mB|Ao@f@o@`@yAbAiBhA{BnAgBbAe@T]PkBx@gBt@mA`@aAXoA^qEhAeCb@kBViCXi@Du@Ds@Dg@Bm@D{@B}@B}@@kCBmCHoAFoAJqAJuANiAPmAPmAT{Cr@wDhAC@w@V{@ZA@kAb@{@ZmA`@w@V}@VaAV}@Ru@Ru@Lu@Nq@Js@J_ALm@HaAJy@HuAHw@D_EJ{@?{AAyAAmAEcV}@gAEmAEsBImAGy@Ce@CsAGuBG_BG}BK{AGaAEmAG_BM{@Kq@KcAOsAWoAYk@O}@W_@Ki@Qm@Sk@Uq@WeAe@q@[u@_@g@Y}AcAaBeAgAy@uAgAkB{AgAeAsAuAiBeB}AwAc@c@g@c@oByAuAcA{AeAkBiAaB}@yAs@iAi@A?eAc@s@Y_Bk@qA_@sBm@_ASkB_@gAQaBU{AOiAI{AI}BIgC?eBBaCHeCPeBJqBFcBBcBBqB?iBEeBGuAKqBQk@G_AM[E]Ga@GQEWCYG}A[sA]y@S}@WwAe@oAc@u@WuAi@eBw@sAo@oBeAkCwA}BiAiEsBoD}AkCcAuCiAwCcAcBk@}Ag@gBg@{@WcAYs@QkCq@aCi@cASaB]eB[cAQgBYwBYiAOeAMmD_@aHk@}@EoDQw@CeAGcACkAAqAE{AEoA?q@?cA?}A@iA@_@@_AFqADu@BiAD{AH}@DuAHaBLYBgAHq@F_@BkDb@o@H}@JcC^MBmBX}B`@cAP{@R_APy@P_ATc@JYFYFe@LmBf@qBj@_Bd@m@Ru@TeBl@sAf@yAh@yAj@mBt@aBp@qBx@mAj@gBx@_CfAs@Zo@\\gAj@y@`@y@b@_Bx@}Ax@sAt@sC~AsBlAg@XOJGBEDs@`@yExCkCdBkAt@gH~EQN_Ap@mDhCyAhASP{AjAqC|BqAdA{ApAiCvBkGvFeGtFmBjBeHhHc@f@i@f@g@j@qLxMqIdKmCfDwBfC{B~BuCrCsDdDuCxBgBlAgC~AmBdAaAh@cBx@eEfByAl@_Bb@]JsEjAYF[Fc@LuB^wBXaAJsBRa@ByBLG@iCFoA?oA?o@?_DGmAGsDWkDc@gCa@}EeAaD_A]MqAa@eBq@sCmA{DaBuQ_IeCeAeDuAyDcBoEkByCoAoJaEqCmAcGgCqDuA}DoA{Bm@i@Mu@SiDs@iB]sBYcBUm@G{@KoAKo@GiAGeAGcAEeBGqCCcBAiBBu@@m@Bo@@u@Bm@Dq@Dw@Fu@Fw@FaAJu@HcANcALeAPcAP_APeAR_B^cARaCp@eDdAiDlAaDpAuB|@oBz@eCbAoClAwGpCqClAwEnB}EpBoBx@a@Na@PuLhFsBz@cGfCeEhBsH`DmBr@wB|@aKfEa@Pc@RwB|@kBv@uB|@yCrA{An@{An@}@^iAf@{Ah@oA^kBf@qAVyARmAN}ANmADoAB}A?oACoAEmAImAMmAQ}@QmAU_AUoA]_A[mAe@}@]m@W[O{@c@iAm@_B_Aa@Wa@YmBuAqBaBeA}@sAqAyB{BaBiBi@k@i@m@g@o@kBwBuAuAqA_BwB_CuBwB{@y@cBwA_CeBeBiA{Ay@eB{@{Ao@}Ao@kBm@oAYaB_@oB]aBO_BMqAIaBE{A?_BDaAD_BJoAL_AL_BZkAR}A`@m@Rk@PqA`@{Ad@aDv@qB`@}Cd@g@Fk@DuCPgABu@AoCKmBKwC[UE{Ca@_AQ"},"start_location":{"lat":52.6045644,"lng":-1.196207},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":427},"duration":{"text":"1 min","value":39},"end_location":{"lat":52.9150088,"lng":-1.3006098},"html_instructions":"At junction <b>25<\/b>, take the <b>A52<\/b> exit to <b>Nottingham<\/b>/<b>Derby<\/b>/<b>Ilkeston<\/b>","maneuver":"ramp-left","polyline":{"points":"mf}aIf||FKL?@E@A@A?C?K?k@CkACK?yAAaB@qGH_@HE@IDIBGDOLMRS\\"},"start_location":{"lat":52.9112691,"lng":-1.3000435},"travel_mode":"DRIVING"},{"distance":{"text":"0.7 km","value":703},"duration":{"text":"1 min","value":77},"end_location":{"lat":52.9176608,"lng":-1.2936819},"html_instructions":"At <b>Sandiacre Interchange<\/b>, take the <b>4th<\/b> exit onto the <b>A52<\/b> ramp to <b>Nottingham<\/b>/<b>llkeston<\/b>","maneuver":"roundabout-left","polyline":{"points":"y}}aIx_}FARATARId@GRKPKPQNSLODO?QCMEUKS[IOKUOg@[sAa@{BUmAWoAG_@C]CUAWFs@?AAkBAc@SyAUyA]yB[{BE_@G_@g@wCMo@AMAME{@"},"start_location":{"lat":52.9150088,"lng":-1.3006098},"travel_mode":"DRIVING"},{"distance":{"text":"1.9 km","value":1892},"duration":{"text":"2 mins","value":94},"end_location":{"lat":52.9202806,"lng":-1.2660063},"html_instructions":"Merge onto <b>Brian Clough Way<\/b>/<b>A52<\/b>","maneuver":"merge","polyline":{"points":"kn~aInt{Fq@kDa@mBUkASeACMa@gCU{AM_AKw@K{@Gm@Gc@Es@CUKoAEw@GcACk@Ey@EcAE}ACeAE{AE_BEoBYsM?G?QWsKAq@A[As@Ck@AUQ{HCyAEmBIwCGqCIeECq@G_DIcDIaDGgDGmBCgBKgEGoCCiACo@EgE"},"start_location":{"lat":52.9176608,"lng":-1.2936819},"travel_mode":"DRIVING"},{"distance":{"text":"29 m","value":29},"duration":{"text":"1 min","value":8},"end_location":{"lat":52.92039690000001,"lng":-1.265618},"html_instructions":"Slight <b>left<\/b> to stay on <b>Brian Clough Way<\/b>/<b>A52<\/b>","maneuver":"turn-slight-left","polyline":{"points":"w~~aIpgvFGg@GUCGCG"},"start_location":{"lat":52.9202806,"lng":-1.2660063},"travel_mode":"DRIVING"},{"distance":{"text":"10 m","value":10},"duration":{"text":"1 min","value":2},"end_location":{"lat":52.9204793,"lng":-1.2655563},"html_instructions":"Turn <b>left<\/b> at the 1st cross street onto <b>Bardills Roundabout<\/b>","maneuver":"turn-left","polyline":{"points":"o__bIbevF?ACAKG"},"start_location":{"lat":52.92039690000001,"lng":-1.265618},"travel_mode":"DRIVING"},{"distance":{"text":"18 m","value":18},"duration":{"text":"1 min","value":2},"end_location":{"lat":52.9205587,"lng":-1.2653171},"html_instructions":"Turn <b>right<\/b> to stay on <b>Bardills Roundabout<\/b>","maneuver":"turn-right","polyline":{"points":"_`_bIvdvFOo@"},"start_location":{"lat":52.9204793,"lng":-1.2655563},"travel_mode":"DRIVING"},{"distance":{"text":"2.0 km","value":1999},"duration":{"text":"2 mins","value":111},"end_location":{"lat":52.93595149999999,"lng":-1.2503376},"html_instructions":"Continue straight onto <b>Brian Clough Way<\/b>/<b>A52<\/b>","maneuver":"straight","polyline":{"points":"o`_bIfcvF?EAC?C?E?C?E?C?CQk@O[[_@]a@m@w@k@q@]a@]c@SU{AiBw@y@cAeAcAcA{@u@gAaAmA_As@i@kDcCwAaAm@_@c@[e@[e@Ya@[y@k@{@o@iA}@i@c@i@g@i@e@[YY[w@u@u@y@eAiAw@_Ac@e@[c@g@o@_AoAkA}Aw@gAcBsBoB{BmAqAcB_Bi@e@cA{@s@k@gBwAe@_@i@a@u@e@_@Ya@SQEa@I"},"start_location":{"lat":52.9205587,"lng":-1.2653171},"travel_mode":"DRIVING"},{"distance":{"text":"44 m","value":44},"duration":{"text":"1 min","value":8},"end_location":{"lat":52.9362372,"lng":-1.2507499},"html_instructions":"Slight <b>left<\/b> toward <b>Ilkeston Rd<\/b>/<b>A6007<\/b>","maneuver":"turn-slight-left","polyline":{"points":"u`bbIresFEXOZIHIHOF"},"start_location":{"lat":52.93595149999999,"lng":-1.2503376},"travel_mode":"DRIVING"},{"distance":{"text":"0.8 km","value":759},"duration":{"text":"1 min","value":73},"end_location":{"lat":52.9407747,"lng":-1.2589361},"html_instructions":"Turn <b>left<\/b> onto <b>Ilkeston Rd<\/b>/<b>A6007<\/b>","maneuver":"turn-left","polyline":{"points":"obbbIdhsFQCETENSd@UpBQbAMh@Ob@]p@ILeBzCi@z@ILU\\_AzAe@t@mAlBq@hAiBrDABwA|Cw@pBqAdDAJAFAH"},"start_location":{"lat":52.9362372,"lng":-1.2507499},"travel_mode":"DRIVING"},{"distance":{"text":"1.8 km","value":1832},"duration":{"text":"2 mins","value":144},"end_location":{"lat":52.9550502,"lng":-1.2463056},"html_instructions":"At the roundabout, take the <b>2nd<\/b> exit onto <b>Coventry Ln<\/b>/<b>A6002<\/b>","maneuver":"roundabout-left","polyline":{"points":"y~bbIj{tF?@?@?@@??@?@?@?@?@?@A@?@?@?@A??@?@A??@A??@A?A?A?AAA??AA??AA??AAA?AAA?A?AAAK]_AgAOQkAkAqAsAcDgDqE{FwAkB_@g@{@gA]]KMi@k@YYaAcA}AyAmAqAu@w@g@i@{@{@]_@u@u@aHeHOOOOqBwBgAgA}AqA]WgAo@y@_@eAi@y@YuE_BMEqAa@eAYIA}@YWMe@Wc@U"},"start_location":{"lat":52.9407747,"lng":-1.2589361},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":320},"duration":{"text":"1 min","value":40},"end_location":{"lat":52.95504409999999,"lng":-1.2415392},"html_instructions":"Turn <b>right<\/b> onto <b>Trowell Rd<\/b>/<b>A609<\/b>","maneuver":"turn-right","polyline":{"points":"axebIllrFDqF@aAH_FC}@EyDCmBA_A"},"start_location":{"lat":52.9550502,"lng":-1.2463056},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":95},"duration":{"text":"1 min","value":19},"end_location":{"lat":52.9541911,"lng":-1.2413929},"html_instructions":"Turn <b>right<\/b> onto <b>Birchwood Rd<\/b>","maneuver":"turn-right","polyline":{"points":"_xebIrnqFhD]"},"start_location":{"lat":52.95504409999999,"lng":-1.2415392},"travel_mode":"DRIVING"},{"distance":{"text":"85 m","value":85},"duration":{"text":"1 min","value":21},"end_location":{"lat":52.9542406,"lng":-1.2401269},"html_instructions":"Turn <b>left<\/b> at the 1st cross street onto <b>Plantation Rd<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-left","polyline":{"points":"urebItmqFI{F"},"start_location":{"lat":52.9541911,"lng":-1.2413929},"travel_mode":"DRIVING"}]
             * traffic_speed_entry : []
             * via_waypoint : []
             */

            private DistanceBean distance;
            private DurationBean duration;
            private String end_address;
            private EndLocationBean end_location;
            private String start_address;
            private StartLocationBean start_location;
            private List<StepsBean> steps;
            private List<?> traffic_speed_entry;
            private List<?> via_waypoint;

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public String getEnd_address() {
                return end_address;
            }

            public void setEnd_address(String end_address) {
                this.end_address = end_address;
            }

            public EndLocationBean getEnd_location() {
                return end_location;
            }

            public void setEnd_location(EndLocationBean end_location) {
                this.end_location = end_location;
            }

            public String getStart_address() {
                return start_address;
            }

            public void setStart_address(String start_address) {
                this.start_address = start_address;
            }

            public StartLocationBean getStart_location() {
                return start_location;
            }

            public void setStart_location(StartLocationBean start_location) {
                this.start_location = start_location;
            }

            public List<StepsBean> getSteps() {
                return steps;
            }

            public void setSteps(List<StepsBean> steps) {
                this.steps = steps;
            }

            public List<?> getTraffic_speed_entry() {
                return traffic_speed_entry;
            }

            public void setTraffic_speed_entry(List<?> traffic_speed_entry) {
                this.traffic_speed_entry = traffic_speed_entry;
            }

            public List<?> getVia_waypoint() {
                return via_waypoint;
            }

            public void setVia_waypoint(List<?> via_waypoint) {
                this.via_waypoint = via_waypoint;
            }

            public static class DistanceBean {
                /**
                 * text : 52.1 km
                 * value : 52081
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationBean {
                /**
                 * text : 44 mins
                 * value : 2615
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class EndLocationBean {
                /**
                 * lat : 52.9542406
                 * lng : -1.2401269
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

            public static class StartLocationBean {
                /**
                 * lat : 52.6358854
                 * lng : -1.2009239
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

            public static class StepsBean {
                /**
                 * distance : {"text":"0.3 km","value":287}
                 * duration : {"text":"1 min","value":61}
                 * end_location : {"lat":52.6339879,"lng":-1.1999181}
                 * html_instructions : Head <b>southwest</b> on <b>Ryder Rd</b> toward <b>Cherry Hills Rd</b>
                 * polyline : {"points":"img`IvpiFHTFNFLFHJJJLPHL@FAH?HANIFCFGHKFKFOJ[DYP}@Jq@Nw@FQJSNKFAH?H?HBFBx@^"}
                 * start_location : {"lat":52.6358854,"lng":-1.2009239}
                 * travel_mode : DRIVING
                 * maneuver : turn-left
                 */

                private DistanceBeanX distance;
                private DurationBeanX duration;
                private EndLocationBeanX end_location;
                private String html_instructions;
                private PolylineBean polyline;
                private StartLocationBeanX start_location;
                private String travel_mode;
                private String maneuver;

                public DistanceBeanX getDistance() {
                    return distance;
                }

                public void setDistance(DistanceBeanX distance) {
                    this.distance = distance;
                }

                public DurationBeanX getDuration() {
                    return duration;
                }

                public void setDuration(DurationBeanX duration) {
                    this.duration = duration;
                }

                public EndLocationBeanX getEnd_location() {
                    return end_location;
                }

                public void setEnd_location(EndLocationBeanX end_location) {
                    this.end_location = end_location;
                }

                public String getHtml_instructions() {
                    return html_instructions;
                }

                public void setHtml_instructions(String html_instructions) {
                    this.html_instructions = html_instructions;
                }

                public PolylineBean getPolyline() {
                    return polyline;
                }

                public void setPolyline(PolylineBean polyline) {
                    this.polyline = polyline;
                }

                public StartLocationBeanX getStart_location() {
                    return start_location;
                }

                public void setStart_location(StartLocationBeanX start_location) {
                    this.start_location = start_location;
                }

                public String getTravel_mode() {
                    return travel_mode;
                }

                public void setTravel_mode(String travel_mode) {
                    this.travel_mode = travel_mode;
                }

                public String getManeuver() {
                    return maneuver;
                }

                public void setManeuver(String maneuver) {
                    this.maneuver = maneuver;
                }

                public static class DistanceBeanX {
                    /**
                     * text : 0.3 km
                     * value : 287
                     */

                    private String text;
                    private int value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public int getValue() {
                        return value;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }
                }

                public static class DurationBeanX {
                    /**
                     * text : 1 min
                     * value : 61
                     */

                    private String text;
                    private int value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public int getValue() {
                        return value;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }
                }

                public static class EndLocationBeanX {
                    /**
                     * lat : 52.6339879
                     * lng : -1.1999181
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

                public static class PolylineBean {
                    /**
                     * points : img`IvpiFHTFNFLFHJJJLPHL@FAH?HANIFCFGHKFKFOJ[DYP}@Jq@Nw@FQJSNKFAH?H?HBFBx@^
                     */

                    private String points;

                    public String getPoints() {
                        return points;
                    }

                    public void setPoints(String points) {
                        this.points = points;
                    }
                }

                public static class StartLocationBeanX {
                    /**
                     * lat : 52.6358854
                     * lng : -1.2009239
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
    }
}
