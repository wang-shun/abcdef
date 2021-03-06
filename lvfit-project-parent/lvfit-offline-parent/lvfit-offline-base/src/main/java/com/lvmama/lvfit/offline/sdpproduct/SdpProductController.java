package com.lvmama.lvfit.offline.sdpproduct;

import org.springframework.ui.Model;

import com.lvmama.lvf.common.dto.BaseResultDto;
import com.lvmama.lvfit.common.dto.sdp.product.FitSdpProductBasicInfoDto;
import com.lvmama.lvfit.common.dto.sdp.product.request.FitSdpProductBasicInfoRequest;
import com.lvmama.lvfit.common.dto.sdp.product.request.FitSdpProductBasicInfoRequestForm;
import com.lvmama.lvfit.common.form.product.FitSdpProductBasicInfoForm;

public interface SdpProductController {
	
		/**显示自主打包产品列表信息.
		 * @param model
		 * @return
		 */
		String showSdpProductList(Model model);
		
		/**查询自主打包产品列表.
		 * @param model
		 * @return
		 */
		BaseResultDto<FitSdpProductBasicInfoForm> querySdpProductList(Model model,FitSdpProductBasicInfoRequestForm requestForm);
		
		
		/**显示自主打包产品列表信息.
		 * @param model
		 * @return
		 */
		String showSdpProductSyncList(Model model);

		/**根据产品ID查询索引列表.
		 * @param model
		 * @return
		 */
		String searchIdIndex(Model model, Long productId);

		/**根据产品ID查询索引同步索引列表.
		 * @param model
		 * @return
		 */
		String searchSynInfo(Model model, Long productId);

		/**根据产品ID查询交通索引列表.
		 * @param model
		 * @return
		 */
		String searchTrafficIndex(Model model, Long productId);

		/**根据产品ID查询出发城市列表.
		 * @param model
		 * @return
		 */
		String searchDepartCity(Model model, Long productId);


		/**根据产品ID查询推送信息列表.
		 * @param model
		 * @return
		 */
		String searchPushInfo(Model model, Long productId);
		
}
