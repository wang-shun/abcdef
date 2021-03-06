package com.lvmama.lvfit.persistence.mybatis.mapper;

import com.lvmama.lvf.common.domain.DomainEntityBaseMapper;
import com.lvmama.lvf.common.domain.DomainEntityByNoMapper;
import com.lvmama.lvf.common.dto.BaseQueryDto;
import com.lvmama.lvf.common.dto.Dto;
import com.lvmama.lvfit.common.dto.order.FitSuppOrderForFlightCallBackDto;
import com.lvmama.lvfit.common.dto.request.FitFliBookingCallBackRequest;
import com.lvmama.lvfit.common.dto.sdp.product.FitSdpCityGroupDto;
import com.lvmama.lvfit.common.form.product.FitSuppOrderForFlightCallBackRequest;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机+X机票回调Mapper
 * @author leizhengwei
 *
 */
public interface FitSuppOrderForFlightCallBackMapper extends DomainEntityBaseMapper<FitSuppOrderForFlightCallBackDto, BaseQueryDto<? extends Dto>>,
	DomainEntityByNoMapper<FitSuppOrderForFlightCallBackDto>
{
	   FitSuppOrderForFlightCallBackDto  loadByVstNos(@Param("vstOrderMainNo") String vstOrderMainNo,@Param("vstOrderNo") String vstOrderNo);

	   List<String>  loadCallRequestStrByVstNo(@Param("vstOrderMainNo") String vstOrderMainNo, @Param("vstOrderNo") String vstOrderNo);

	   List<FitSuppOrderForFlightCallBackDto> queryAll(BaseQueryDto<FitSuppOrderForFlightCallBackRequest> dto);

	   int querySuppOrderForFlightCallBackCounts(BaseQueryDto<FitSuppOrderForFlightCallBackRequest> baseQuery);

	   List<FitFliBookingCallBackRequest> queryByVstMainNo(BaseQueryDto<FitFliBookingCallBackRequest> baseQuery);

}
