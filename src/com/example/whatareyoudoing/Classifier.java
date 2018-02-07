package com.example.whatareyoudoing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.Random;

//import javafx.scene.control.Labeled;

//import org.omg.CORBA.PUBLIC_MEMBER;









import weka.classifiers.trees.RandomForest;
import weka.core.Instances;



public class Classifier {
	/*
	private static Resources getResources(){
		Resources mResources =null;
		mResources = getResources();
		return mResources;
	}*/
	public static double evaluate(InputStream is, String tes) throws FileNotFoundException, IOException,Exception 
	{
		System.out.println("555"+tes);
		Instances test = new Instances(
                new BufferedReader(
   		     new FileReader(tes)));
		test.setClassIndex(test.numAttributes()-1);
		System.out.println("666");

	//    Reader in = new InputStreamReader(is);
     //   Instances train = new Instances(
     //           new BufferedReader(in));
	
	//	Instances train = new Instances(
	//                     new BufferedReader(
	//        		      new FileReader(tra)));
		
	//	train.setClassIndex(train.numAttributes()-1);
		
	//	Instances result=new Instances(test);
		
	//	RandomForest tree= new RandomForest();
	//	tree.buildClassifier(train);

		ObjectInputStream ois = new ObjectInputStream(is); 
		RandomForest tree = (RandomForest)ois.readObject();
		ois.close();
		System.out.println("777");

		
		double cla= tree.classifyInstance(test.instance(0));
		System.out.println("cla" + cla);

		return cla;
/*		for(int i=0;i<test.numInstances();i++)
		{
			double clsLabel = tree.classifyInstance(test.instance(i));
			result.instance(i).setClassValue(clsLabel);
		}
		
		BufferedWriter writer = new BufferedWriter(
				               new FileWriter(res));
		writer.write(result.toString());
		writer.newLine();
		writer.flush();
		writer.close();
	//	System.out.println(tree.toString());//������������
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(tree, test);
		System.out.println(eval.toSummaryString("\nResults\n====\n", false));
		//������������������
	//	System.out.println(tree.classifyInstance(test.instance(6)));
	*/
	}
	
	
			
	

}
