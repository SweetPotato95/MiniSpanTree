package MiniSpanTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import euclideanMetric.EuclideanMetric;


public class MiniSpanTree {  
    /** 
     * 求最小树的Kruskal算法 
     * 算法思想：克鲁斯卡尔算法从另一个途径求网中的最小生成树。假设联通网N=(V,{E})，则令 
     * 最小生成树的厨师状态为只有n个顶点而无边的非连通图T=(V,{})，途中每个顶点自称一个连通分量。 
     * 在E中选择代价最小的边，若该边衣服的顶点落在T中不同的连通分量上，则将此边加入到T中，否则舍去此边 
     * 而选择下一条最小的边。以此类推，直至T中所有的顶点都在同一连通分量上为止。 
     * @param V 图中的节点集合 
     * @param E 图中边的集合 
     */
	static List<List<Double>> pointList = new ArrayList<List<Double>>();
    static String dataPath = "C:\\Graduate Design\\MiniSpanTree\\data.txt";
    static String modelPath = "C:\\Graduate Design\\MiniSpanTree\\model.txt";
    static String testPath = "C:\\Graduate Design\\MiniSpanTree\\testPoints.txt";
    static int[] V;
    static Edge[] E;
    static ArrayList<HashSet> sets=new ArrayList<HashSet>();
    static ArrayList<Double> distance = new ArrayList<Double>();
    
	static public void initData() throws NumberFormatException, IOException{
		FileReader fr = new FileReader(dataPath);
		BufferedReader bufferedReader = new BufferedReader(fr);  
        String line = null;  
        int i = 0;  
        while ((line = bufferedReader.readLine()) != null) {  
            String[] s = line.split(" ");  
            List<Double> list = new ArrayList<Double>();  
            for (String string : s) {  
                    list.add(Double.parseDouble(string));  
            }
            i++;
            pointList.add(list);
            
        }
        V = new int[i];
        List<Edge> Eset =new ArrayList<Edge>();
        for(int k = 0;k<i;k++){
        	V[k] = k;
        	for(int j = k+1;j<i;j++){
        		System.out.println(k+","+j);
        		Eset.add(new Edge(k,j,EuclideanMetric.sim_distance(pointList.get(k), pointList.get(j))));
        	}
        }
        
        E = new Edge[Eset.size()];
        for(int j = 0;j<Eset.size();j++){
        	E[j] = Eset.get(j);
        }
    }
    public static void KRUSKAL(double val){  
        Arrays.sort(E);//将边按照权重w升序排序  
        for(int i=0;i<V.length;i++){  
            HashSet set=new HashSet();  
            set.add(V[i]);  
            sets.add(set);
            distance.add((double) 0);
        }
        for(int i=0;i<E.length;i++){ 
            if(E[i].w>val){
            	break;
            }
        	int start=E[i].i,end=E[i].j;  
            
            int counti=-1,countj=-2;  
            for(int j=0;j<sets.size();j++){  
                HashSet set=sets.get(j);
                if(set.contains(start)){  
                    counti=j;  
                }  
                      
                if(set.contains(end)){  
                    countj=j;  
                }  
            }
            
            
            if(counti<0||countj<0){
            	continue;
            }
            else{
            if(counti!=countj){ 
            	//System.out.println("输出start="+start+"||end="+end+"||w="+E[i].w);  
                if(countj<counti){
                	int tmp = countj;
                	countj = counti;
                	counti = tmp;
                	
                }
            	HashSet setj=sets.get(countj);  
                sets.remove(countj);  
                HashSet seti=sets.get(counti);  
                sets.remove(counti);  
                seti.addAll(setj);  
                sets.add(seti);
                
                distance.remove(countj);
                distance.remove(counti);
                distance.add(E[i].w);
                //treeEdges.add(E[i]);
            }else{  
                //System.out.println("他们在一棵子树中，不能输出start="+start+"||end="+end+"||w="+E[i].w);  
  
            }  
        }
        }
        /*for(int i = 0 ;i<treeEdges.size();i++){
        	System.out.print("treeEdge"+i);
        	Edge tmp = treeEdges.get(i);
        	System.out.println(":start="+tmp.i+"||end="+tmp.j+"||w="+tmp.w); 
        }*/
        //System.out.println(sets);  
        //System.out.println(distance);
          
    }
    public static void save() throws IOException{
    	FileWriter fw = new FileWriter(modelPath);
    	BufferedWriter bw = new BufferedWriter(fw);
    	for(int i = 0;i<distance.size();i++){
    		HashSet tmp = sets.get(i);
    		Object[] tmpArr = new Object[tmp.size()];
    		tmp.toArray(tmpArr);
    		fw.write(i+","+tmpArr.length+","+distance.get(i)+"\r\n");
    		
    		for(int j = 0;j<tmpArr.length;j++){
    			int tmpIndex = (int) tmpArr[j];
    			List<Double> tmpPoint = pointList.get(tmpIndex);
    			fw.write(String.valueOf(i));
    			for(int k = 0;k<tmpPoint.size();k++){
    				fw.write(","+tmpPoint.get(k));
    			}
    			fw.write("\r\n");
    		}
    		
    	}
    	bw.flush();
    	bw.close();
    }

    public static void main(String [] args) throws NumberFormatException, IOException{
    	//Initial data
    	MiniSpanTree.initData();
        MiniSpanTree.KRUSKAL(1);
        MiniSpanTree.save();
        
        //Test Process
        Judge judge = new Judge();
        judge.loadData();
        FileReader fr = new FileReader(testPath);
        BufferedReader br = new BufferedReader(fr);
        String tmpLine = "";
        while((tmpLine = br.readLine())!=null){
        	String[] tmpList = tmpLine.split(" ");
        	List<Double> askPoint = new ArrayList<Double>();
            for(int i = 0;i<tmpList.length;i++){
            	askPoint.add(Double.parseDouble(tmpList[i]));
            }
            judge.getPointClass(askPoint);
        }
    }  
  
}  