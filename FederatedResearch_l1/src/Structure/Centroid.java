package Structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Centroid implements Serializable{
	private HashMap<String, Double>termMap = new HashMap<String, Double>();

	public HashMap<String, Double> getTermMap() {
		return termMap;
	}

	public void setTermMap(HashMap<String, Double> termMap) {
		this.termMap = termMap;
	}

	/**
	 * 根据一个数据集更新质心
	 * @param docList
	 */
	public Centroid(ArrayList<String> docList) {
		//TODO
	}
	/**
	 * 返回所有词项词频之和
	 * @return
	 */
	public double tokenierCount(){
		//TODO
		return 0.0;
	}

}
