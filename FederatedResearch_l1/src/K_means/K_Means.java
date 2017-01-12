package K_means;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.terrier.structures.Index;

import Structure.Centroid;
import Structure.SingleDoc;

public class K_Means {

	private static final double DISTance = 0;

	public static int ITERATION = 10;
	public static double lambda = 0.5;
	
	private ArrayList<String> docList = new ArrayList<String>();
	//所有<文档ID，对应的文档信息词汇等>
	private HashMap<String, SingleDoc> AllDocMap = new HashMap<String, SingleDoc>();
	//所有类别，及类别中的信息
	private ArrayList<Centroid>  centroidList = null; 
	//所有<分属的类别，文档ID-List>
	private HashMap<Integer, ArrayList<String>> TypeDocList = new HashMap<Integer, ArrayList<String>>();
	
	public static int currentIteration = 0;
	
	public K_Means(){
		docList = new ArrayList<String>();
		index = Index.createIndex();
	}
	public K_Means(ArrayList<String> al , Index index){
		this.docList = al;
		this.index = index;
	}
	public void initial(){
	
		
	}
	/**
	 * k-means方法的整体流程
	 */
	public void run(ArrayList<Centroid>  centroidListParam){
		ArrayList<Centroid>  tempCentroidList = centroidListParam;
		if (centroidListParam == null || centroidListParam.size() == 0){
			tempCentroidList = new ArrayList<Centroid>();
		}
		
		for (int iteration = 0; iteration < K_Means.ITERATION; iteration++){
			currentIteration = iteration;
			//每次迭代开始，清空记录表
			TypeDocList = new HashMap<Integer, ArrayList<String>>();
			record();
			//是否满足终止条件
			if ( isEnd(tempCentroidList) ){
				return;
			}
			for (int docNo = 0; docNo < docList.size(); docNo ++){
				String docString = docList.get(docNo);
				//计算文档与各个中心之间的距离
				double minDouble = Double.MAX_VALUE;
				int cenIndex = 0;
				//选择合适的中心
				for (int cenNo = 0; cenNo < centroidList.size(); cenNo ++){
					double tempDistance = distance(docList.get(docNo), centroidList.get(cenNo));
					if (minDouble > tempDistance){
						minDouble = tempDistance;
						cenIndex = cenNo;
					}
				}
				//将文档加入新类中
				ArrayList<String> tempAList = null;
				if (TypeDocList.containsKey(cenIndex)){
					tempAList = TypeDocList.get(cenIndex);
				}else {
					tempAList = new ArrayList<String>();
				}
				tempAList.add(docString);
				TypeDocList.put(cenIndex, tempAList);
			}
			//更新质心
			refreshAllCentroid();
		}
	}
	/*
	 * 应当结束就为真
	 */
	private boolean isEnd(ArrayList<Centroid>  tempCentroidList){
		//TODO
		for (int i = 0; i < tempCentroidList.size(); i ++){
			double dist = distance(this.centroidList.get(i), tempCentroidList.get(i)) ;
			if (dist > K_Means.DISTance){
				return false;
			}
		}
		return true;
	}
	/**
	 * 计算质心的距离
	 * @param centroid
	 * @param centroid2
	 * @return
	 */
	private double distance(Centroid centroid, Centroid centroid2) {
		if (centroid.getTermMap() != null && centroid2.getTermMap()!=null){
			return distance(centroid.getTermMap(), centroid.tokenierCount(), centroid2.getTermMap(), centroid2.tokenierCount());
		}
		return 0;
	}
	//计算某个文档和质心的距离
	private double distance(String string, Centroid centroid){
		return distance(AllDocMap.get(string).getTermMap(), AllDocMap.get(string).tokenierCount(), centroid.getTermMap(), centroid.tokenierCount());
	}
	public double distance (HashMap<String, Double> map1, double tokenier1, HashMap<String, Double>map2, double tokenier2){
		Set<String> termSet1 = map1.keySet();
		Iterator<String> iterator = termSet1.iterator();
		double result = 0.0;
		while (iterator.hasNext()){
			String term = iterator.next();
			if (!map2.containsKey(term)){
				continue;
			}
			double count1 = map1.get(term);
			double count2 = map2.get(term);
			//计算变量
			double pcw = count2 / tokenier2;
			double pbw = computPBW(term);
			double pdw = (1 - K_Means.lambda) * count1/tokenier1 + K_Means.lambda * pbw;
			//第一部分
			double part1 = pcw * Math.log(pdw / (K_Means.lambda * pbw));
			double part2 = pdw * Math.log(pcw / (K_Means.lambda * pbw));
			result += part1 + part2;
		}
		return result;
	}
	//更新全部质心
	private void refreshAllCentroid() {
		for (int cenNo = 0; cenNo < centroidList.size(); cenNo ++){
			refreshSingleCentroid(cenNo);
		}
	}
	/**
	 * 更新单个质心
	 * @param cenNo
	 */
	private void refreshSingleCentroid(int cenNo){
		ArrayList<String> docList = TypeDocList.get(cenNo);
		Centroid centroid = new Centroid(docList);
		centroidList.set(cenNo, centroid);
		return;
	}
	/**
	 * 每次迭代完了，记录一些数据，保存一下断点
	 * 需要记录：1：质心集合列表，
	 * 					2：所有文档的类别和质心
	 */
	private void record(){
		ToolsIO.write(centroidList);
		ToolsIO.write(TypeDocList);		
	}
	/**
	 * 有问题
	 * @param term
	 * @return
	 */
	private double computPBW(String term){
		//TODO
		double frequence = 0.0;
		double allcount = 0.0;
		for(int i = 0; i < centroidList.size(); i ++){
			allcount += centroidList.get(i).tokenierCount();
			frequence +=centroidList.get(i).getTermMap().get(term);
		}
		return frequence/allcount;
	}
}















