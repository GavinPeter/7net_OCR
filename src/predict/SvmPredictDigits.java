package predict;

import java.awt.image.BufferedImage;

import java.net.URL;

import javax.imageio.ImageIO;

import norm.Normalize;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;


/*  This is for predict digit 
 *  use trained digitSVM.model
 */  

public class SvmPredictDigits {

public static void main(String[] args) throws Exception {
	
	//caculate computing time
	long time1, time2 ;
	 
    time1 = System.currentTimeMillis();
	
	BufferedImage coloredImage;

	BufferedImage imgs[]; 
	
	svm_model model = svm.svm_load_model("./digitSVM.model");
	
	URL imageURL = new URL("https://www.7net.com.tw/7net/7memberlogin.faces?modelhan=t&random_value=0.25135172437876463");
	   
	   coloredImage = ImageIO.read(imageURL);
	   
	   imgs = Normalize.imageNormalize(coloredImage);
	   
	   
	    for (int i = 0; i < imgs.length; i++) { 
	    	
            
			svm_node[] x = new svm_node[imgs[i].getHeight()*imgs[i].getWidth()];
			
			 for (int h=0; h< imgs[i].getHeight(); h++ ){ 
				 	for (int w=0; w< imgs[i].getWidth(); w++){

				 		x[h*imgs[i].getWidth()+w] = new svm_node();
				 		
				 		x[h*imgs[i].getWidth()+w].index = h*imgs[i].getWidth()+w+1; 
				 						 		
				 		x[h*imgs[i].getWidth()+w].value =  (double)(imgs[i].getRGB(w, h) & 0xFF);

				    }
			}
			 
			 
			 
			 System.out.println("digit "+i+" : "+ svm.svm_predict(model,x) );
             
			 
        } 
	    System.out.println("check save.jpg and see the prediction upon" );
	    
	    time2 = System.currentTimeMillis();
	    
	    System.out.println("Recognize digit takes ¡G" + (time2-time1)/1000 + " sec");
		   
	}
	
}
