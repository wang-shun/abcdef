package com.lvmama.lvf.common.dto.status.order;

import java.util.ArrayList;
import java.util.List;

import com.lvmama.lvf.common.dto.status.Op;
import com.lvmama.lvf.common.dto.status.OpCommandBack;
import com.lvmama.lvf.common.dto.status.OpCommandFront;
import com.lvmama.lvf.common.dto.status.OpList;
import com.lvmama.lvf.common.dto.status.engine.GeneratorOp;
import com.lvmama.lvf.common.dto.status.engine.OpContext;
import com.lvmama.lvf.common.dto.status.engine.ProcessOp;
import com.lvmama.lvf.common.dto.status.engine.StatusContext;

/**
 * 订单取消状态
 * @author majun
 * @date   2015-1-30
 */
public enum OrderCancelStatus  implements GeneratorOp,ProcessOp {

	NULL(""){
		public boolean vaild(StatusContext statusContext){		
			return OpCommandBack.ROLLBACK_CANCEL.equals(statusContext.getOp());
		}
	},
	APPLY_CANCEL("申请取消"){
		public boolean vaild(StatusContext statusContext){		
			return OpCommandBack.APPLY_CANCEL.equals(statusContext.getOp());
		}	
	},
	CANCEL_FAIL("取消失败"){
		public boolean vaild(StatusContext statusContext){		
			return OpCommandBack.OP_REJECT_CANCEL.equals(statusContext.getOp());
		}	
	},
	CANCEL_SUCC("已取消"){
		public boolean vaild(StatusContext statusContext){	
			return OpCommandBack.OP_PASS_CANCEL.equals(statusContext.getOp());
		}
		public List<Op> generatorFrontOp(OpContext opContext){
			OpList opList = new OpList(opContext);
			opList.add(OpCommandFront.APPLY_HIDDEN);
			return opList.list();
		}
        public List<Op> generatorBackOp(OpContext opContext) {
            OpList opList = new OpList(opContext);
            opList.add(OpCommandBack.APPLY_REFUND);
            opList.add(OpCommandBack.ROLLBACK_CANCEL);
            return opList.list();
        }
	};
	
	private String cnName;

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	private OrderCancelStatus(String cnName) {
		this.cnName = cnName;
	}
	public List<Op> generatorFrontOp(OpContext opContext){
		return new ArrayList<Op>();
	};
	
	public List<Op> generatorBackOp(OpContext opContext){
		return new ArrayList<Op>();
	};
	
	public List<Op> generatorOp(OpContext opContext){

		switch (opContext.getOpSource()) {
		case FRONT:
			return generatorFrontOp(opContext);
		case BACK:
			return generatorBackOp(opContext);
		default:
			return new ArrayList<Op>();
		}
	}


	@Override
	public boolean vaild(StatusContext statusContext) {
		return false;
	}

	@Override
	public void processOp(StatusContext statusContext) {
		if(statusContext.getOrder().getFlightOrderStatus() == null)
			return;
		
		if(vaild(statusContext)){
			statusContext.getOrder().getFlightOrderStatus().setOrderCancelStatus(this);
		}
	};

}
