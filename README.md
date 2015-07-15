# 7net_OCR
Research Purpose only

Normalize part:
1. split original image to four 20x20 images
2.  folders contain  0-9 digit respectively

Train part:
1.use normalize part to train svm model from upon folder 

Predict part:
1.use the model to predict number
2.check the answser save.jpg

<b>predict result<br />
digit 0 : 1.0<br />
digit 1 : 6.0<br />
digit 2 : 5.0<br />
digit 3 : 8.0<br />
check save.jpg and see the prediction upon<br />
Recognize digit takes ：1 sec<br />
</b>