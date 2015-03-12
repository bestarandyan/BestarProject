/**
 * 
 */
package com.manyi.mall.cachebean.mine;

import java.util.List;

import com.huoqiu.framework.rest.Response;

/**
 * @author bestar
 * 
 */
public class FootprintListResponse extends Response {

	public List<CheckedListResponse> result;

	

	public List<CheckedListResponse> getResult() {
		return result;
	}

	public void setResult(List<CheckedListResponse> result) {
		this.result = result;
	}

	public static class CheckedListResponse {

		private String companyName;

		private String cityName;

		List<CheckedResponse> examineRecodList;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public List<CheckedResponse> getExamineRecodList() {
			return examineRecodList;
		}

		public void setExamineRecodList(List<CheckedResponse> examineRecodList) {
			this.examineRecodList = examineRecodList;
		}

	}

	public static class CheckedResponse {

		private String imgUrl;
		private String productName;
		private float price;
		private Long clickCount;
		private Long visitCount;
		private Long priaseCount;
		private int hasVoucher;// 是否可用代金券

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public Long getClickCount() {
            return clickCount;
        }

        public void setClickCount(Long clickCount) {
            this.clickCount = clickCount;
        }

        public Long getVisitCount() {
            return visitCount;
        }

        public void setVisitCount(Long visitCount) {
            this.visitCount = visitCount;
        }

        public Long getPriaseCount() {
            return priaseCount;
        }

        public void setPriaseCount(Long priaseCount) {
            this.priaseCount = priaseCount;
        }

        public int getHasVoucher() {
            return hasVoucher;
        }

        public void setHasVoucher(int hasVoucher) {
            this.hasVoucher = hasVoucher;
        }
    }

}
