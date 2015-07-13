package train;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

import libsvm.*;


/*  This is for traing digit 
 *  train and save the model used by predicting later
 */  

public class SvmTrainDigit {
	
	public static void main(String[] args) throws Exception {
		
		svm_model model = svmTrain();

		svm.svm_save_model("./digitSVM.model",model);
	}
	
	
	private static svm_model svmTrain() throws Exception {
		
		BufferedImage digitImage;
		
		// each image array store as svm_node()
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		
		//store classes  
		Vector<Double> vy = new Vector<Double>();
		
		//read files for digit 0 to 9 directory    
		for (int i=0; i<10; i++){
			for (final File fileEntry : new File("./" +i).listFiles()) {
				
				//System.out.println(fileEntry.getName());
				
				digitImage = ImageIO.read(new File("./"+ i +"/" + fileEntry.getName()));
				
				vy.addElement((double)i);
				
				//svm_node[] for 20*20 size
				svm_node[] x = new svm_node[digitImage.getHeight()*digitImage.getWidth()];
				
				 for (int h=0; h< digitImage.getHeight(); h++ ){ 
					 	for (int w=0; w< digitImage.getWidth(); w++){

					 		//pixel set new node according to example setting
					 		x[h*digitImage.getWidth()+w] = new svm_node();
					 		
					 		//index start from 1 
					 		x[h*digitImage.getWidth()+w].index = h*digitImage.getWidth()+w+1; 
					 		
					 		//svm_node[] value to set
					 		x[h*digitImage.getWidth()+w].value =  (double)(digitImage.getRGB(w, h) & 0xFF);

					    }
				}
				 //add to data vector
				 vx.addElement(x); 
			}
		}
		svm_problem prob = new svm_problem(); 
		
		prob.l = vy.size(); // how many sizes is training data  //vy.size()  //training data row number
	
		prob.x = new svm_node[prob.l][];  //svm_node[row number][] 
		prob.y = new double[prob.l];      //new row number size
		for( int i = 0; i< prob.l; i++ ){
			prob.x[i] = vx.elementAt( i );  //get that row data  svm_node  x.index x.value
			prob.y[i] = vy.elementAt( i );  //vy class label is prob.y[i]
		}
				
		
	    svm_parameter param = new svm_parameter();
	    
		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.LINEAR;
		param.degree = 3;
		param.gamma =1/400;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
	    svm_model model = svm.svm_train(prob, param);

	    return model;
	}
}
