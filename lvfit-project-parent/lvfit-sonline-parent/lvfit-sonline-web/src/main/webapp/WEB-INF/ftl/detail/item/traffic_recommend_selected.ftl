
<div class="adjust-item clearfix" id="139178_1723273">
    <div class="need-submit-data" data-group="traffic" data-goodsid="2963379" data-goodtype="traffic" data-mainitem="MAIN" data-routerelation="PACK" data-detailid="524827" data-quantity="1" data-todate="2016-05-04" data-visittime="2016-05-04" data-backdate="2016-05-08">
    </div>
    <div class="title">
        <span>交通</span>
    </div>
    <#list charterFlightInfos as arvFlightInfo>  
    <div class="right-container clearfix" go-flightno="${arvFlightInfo.flightNo}" back-flightno="${arvFlightInfo.returnFlightInfoDto[0].flightNo}"
          <#if arvFlightInfo_index != 0> style="display:none"</#if> tag='charsetflight'>
        <div class="type gray">
                    <div class="clearfix">
                            <p id="toback" class="shadow">往返</p>
                    </div> 
                    <dl class="ldt_tcil_content clearfix">
                        <dt>
                            <em>出发：</em>
                        </dt>
                        <dd>
                            <p>
                                ${arvFlightInfo.departureCityName}
                            </p>

                            <p>
                                ${arvFlightInfo.departureDate}
                            </p>
                        </dd>
                    </dl>
                    <dl class="ldt_tcil_content clearfix">
                        <dt><em>返回：</em></dt>
                        <dd>
                            <p>
                                ${arvFlightInfo.returnFlightInfoDto[0].departureCityName}
                            </p>

                            <p>
                                ${arvFlightInfo.returnFlightInfoDto[0].departureDate}
                            </p>
                        </dd>
                    </dl>
                    <!-- TODO add end -->
                </div>
        <div class="detail">
                 <ul class="ldt_tci_center">
                            <!--不含中转 开始--> 
                                <li class="ldt_tcic_info ldt_tcic_info_first clearfix">

                                <div class="ldt_tcici_icon">
                                    <span class="ldt_tcici_icon_go">去</span>
                                </div>
                                <div class="ldt_tcici_clock">
                                    <b>${arvFlightInfo.departureTime?substring(11, 16)}</b>
                                    <b>${arvFlightInfo.arrivalTime?substring(11, 16)}</b>
                                </div>
                                <div class="ldt_tcici_fromto">
                                    <p>${arvFlightInfo.departureAirportName}
                                    </p>
                                    <p></p>
                                </div>
                                <div class="ldt_tcici_time">
                                        ${arvFlightInfo.flyTime}
                                </div>
                                <div class="ldt_tcici_company">
                                    ${arvFlightInfo.carrierName}<br>
                                    ${arvFlightInfo.flightNo}
                                </div>
                                <div class="ldt_tcici_type"> 
                                      ${arvFlightInfo.seats[0].seatClassTypeName}  
                                </div>
                                     </li></ul>
                       <ul class="ldt_tci_center">
                                <li class="ldt_tcic_info ldt_tcic_info_first clearfix">
                                <div class="ldt_tcici_icon">
                                <span class="ldt_tcici_icon_go">返</span>
                                </div>
                                <div class="ldt_tcici_clock">
                                    <b>${arvFlightInfo.returnFlightInfoDto[0].departureTime?substring(11, 16)}</b>
                                    <b>${arvFlightInfo.returnFlightInfoDto[0].arrivalTime?substring(11, 16)}</b>
                                </div>
                                <div class="ldt_tcici_fromto">
                                    <p>${arvFlightInfo.returnFlightInfoDto[0].departureAirportName}
                                    </p>
                                    <p></p>
                                </div>
                                <div class="ldt_tcici_time">
                                        ${arvFlightInfo.returnFlightInfoDto[0].flyTime}
                                </div>
                                <div class="ldt_tcici_company">
                                    ${arvFlightInfo.returnFlightInfoDto[0].carrierName}<br>
                                    ${arvFlightInfo.returnFlightInfoDto[0].flightNo}
                                </div>
                                <div class="ldt_tcici_type">   
                                        ${arvFlightInfo.returnFlightInfoDto[0].seats[0].seatClassTypeName}  
                                </div>
                            </li>
                      </ul>
                          
                        <!-- TODO add end -->
                        <div class="default">
                            <div class="adjust-traffic-item adjust-product-item" data-price="58" data-item-id="1">
                                <div class="adjust-traffic-item-type">
                                    <span></span>
                                </div>
                                <div class="adjust-traffic-item-price">
                        <span class="price">
                        </span>
                         </div>
                                <div class="adjust-traffic-item-price">
                        <span class="price">
                            <select class="lvmama-price-flag" data-price-1="105000" hidden="hidden">
                                <option value="1">1</option>
                            </select>
                        </span>
                                </div> 
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                    <div class="replace">
                        <button class="replace-button flight-replace-button">更换其他交通</button>
                    </div> 
    </div>
    </#list>
</div>
