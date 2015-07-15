package norm;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/*  divide from origin image to 4 digit  
 *  and normalize to 20*20
 */  

public class Normalize
{
	public static void main(String[] args) throws Exception {
	  
		String binaryImgName;
		
		String[] digits;
		
		BufferedImage coloredImage;
   
	    BufferedImage imgs[]; 
		
		for (final File fileEntry : new File("./img2").listFiles()) {
		       
            System.out.println(fileEntry.getName());
            
            binaryImgName = fileEntry.getName().replaceFirst("[.][^.]+$", ""); 
            
            digits = binaryImgName.split("(?!^)");
            
            coloredImage = ImageIO.read(new File("./img2/" + fileEntry.getName()));
            
            imgs = imageNormalize(coloredImage);
            
            for (int i = 0; i < imgs.length; i++) { 
            	ImageIO.write(resize( imgs[i], 20, 20), "jpg", new File( digits[i] + "/" + binaryImgName +"_"+ i + ".jpg"));  
            }
        
            
			}
		}
		
	//project method for vertical and horizontal
	public static int[] projectMethod(BufferedImage blackNWhite, int vh ){
		  
			int[] prj ;
 		  
			if (vh ==0){
				prj = new int[blackNWhite.getHeight()];
				 for (int h=0; h<blackNWhite.getHeight(); h++ ){ 
					 	for (int w=0; w< blackNWhite.getWidth(); w++){
					    	 prj[h] += ((blackNWhite.getRGB(w, h) & 0xFF) ==0) ? 1 : 0; 
					    }
				}
				
			}
			else
			{
				prj = new int[blackNWhite.getWidth()];
				for (int w=0; w< blackNWhite.getWidth(); w++){
				  for (int h=0; h<blackNWhite.getHeight(); h++ ){
					    	 prj[w] += ((blackNWhite.getRGB(w, h) & 0xFF) ==0) ? 1 : 0; 
					    }
				}
			}
 		  
		  
 	
 		  return prj;
	}
	
		//image resize
	   public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
		    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage( newW, newH, img.getType() );

		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();

		    return dimg;
		}
	   
	   //image spilt and resize for normalize 
	   public static BufferedImage[] imageNormalize(BufferedImage coloredImage )throws Exception{
		   
		   ImageIO.write(coloredImage, "jpg", new File("save.jpg"));
		   
		   BufferedImage blackNWhite = new BufferedImage(coloredImage.getWidth(),coloredImage.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
		   Graphics2D graphics = blackNWhite.createGraphics();
		   graphics.drawImage(coloredImage, 0, 0, null);
		
		   int[] histo =  Normalize.projectMethod(blackNWhite, 1 );
			
		   int prePosVal =0;
		   
		   int count =0;
			 
		   boolean xorPrePos = false;

		   boolean xorNowPos = false;
			  
		   int pos[] = new int[8];
		   
		   BufferedImage imgs[] = new BufferedImage[4];
		   
		   		//project to horizon then split digit image  
			    for (int w = 0; w < blackNWhite.getWidth(); w++) {
			    	xorPrePos = prePosVal!=0? true: false;
			    	
			    	xorNowPos = histo[w] !=0 ? true: false; 
			    	
			    		if (  xorPrePos ^ xorNowPos ){
			    			if (count > 7){
			    				System.out.println("something wrong!!");
			    				for (int i = 0; i < histo.length; i++) {
			    					System.out.println( i+":"+ histo[i]);
			    				}
			    				System.out.println( pos[0] +" " + pos[1]  +" " + pos[2] + " " + pos[3] + " " + pos[4] + " " + pos[5] + " " + pos[6] + " " + pos[7]);
			    				break;
			    			}
			    					
			    			pos[count] = w;
			    			count++;
				    		
			    			//for ugly denoise purpose 
			    			if ((count ==2||count==4)||(count ==6||count==8)){
				    			if ((pos[count-1] - pos[count-2]) <5)
				    				count = count -2;			    			
				    		}
			    		}
			    		prePosVal = histo[w];		    		
		        }
			    
			    
			    
			    //get binary four digit image
			    for (int i=0; i < 4;i++ ){
			    	
		            imgs[i] = new BufferedImage( pos[i*2+1]-pos[i*2], blackNWhite.getHeight(), blackNWhite.getType());  

		            // draws the image chunk  
		            Graphics2D gr = imgs[i].createGraphics();  
		            
		            gr.drawImage( blackNWhite, 0, 0, pos[i*2+1]-pos[i*2], blackNWhite.getHeight(), pos[i*2], 0, pos[i*2+1], blackNWhite.getHeight(), null );  
		            
		            gr.dispose(); 	
			    }
		       
			    int[] horiz;
			    
			    for (int i = 0; i < imgs.length; i++) { 
			    	
			    	horiz= Normalize.projectMethod(imgs[i], 0);

			 		  
			    	for (int h = 0; h < blackNWhite.getHeight(); h++) {
			    		if ( horiz[h]!=0 ){
			    			pos[0]=h;
			    			break;
			    		}
			    			
			    	}
			    	
			    	for (int h = blackNWhite.getHeight()-1; h >0; h--) {
			    		if ( horiz[h]!=0 ){
			    			pos[1]= h;
			    			break;
			    		}
			    	}
			    	
			    	BufferedImage nWhite = new BufferedImage( imgs[i].getWidth(), pos[1]- pos[0], imgs[i].getType());  
			    	
			    	Graphics2D gr = nWhite.createGraphics();  
		            
		            gr.drawImage( imgs[i], 0, 0, imgs[i].getWidth(), pos[1]- pos[0], 0, pos[0], imgs[i].getWidth(), pos[1], null );  
		            
		            gr.dispose(); 		    	
		            
		            imgs[i] = Normalize.resize( nWhite, 20, 20);
		            
		        }
			    return imgs;
	   }

	
}
