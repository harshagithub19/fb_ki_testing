package com.varun.fbproj.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javassist.bytecode.Descriptor.Iterator;

import com.varun.fbproj.model.Comment;
import com.varun.fbproj.model.Status;

public class StatusService {

	DBAccess db= new DBAccess();
	
	 public boolean addStatus(Status s1){
		  try{
			 boolean check=false;
			 while(check!=true){
				 System.out.println("trying connection");
				check= db.start();
			 }
			 String  status_desc=s1.getStatus_desc(); 
			 String emailID=s1.getEmailID();
			 
			 String query = "insert into status(status_desc,emailID) values(?, ?)";
			 PreparedStatement pstmnt = db.con.prepareStatement(query);
			 pstmnt.setString(1,status_desc);
			 pstmnt.setString(2,emailID);
			 pstmnt.executeUpdate();
			 db.stop();
			 return true;
		  }
		  catch (Exception e) 
	      {
	          System.out.println(e.getMessage());
	      }
		 return false;  
	   }//add status ends here
	 
	 //abhi incomplete h ye removeStatus
	 public boolean removeStatus(Status s1){
		 try{
			 boolean check=false;
			 while(check!=true){
				 System.out.println("trying connection");
				check= db.start();
			 }
			 //String status_id=s1.getStatusID();
			 String query = "UPDATE status SET flag=? where statusID = ?";
	         PreparedStatement pstmnt = db.con.prepareStatement(query);
	         pstmnt.setInt(1,0);
			 pstmnt.setInt(2,s1.getStatusID());
			 pstmnt.executeUpdate();
			
			 db.stop();
			 return true;
		  }
		  catch (Exception e) 
	      {
	          System.out.println(e.getMessage());
	      }
		 return false;  
	   }//removeStatus ends here
	 
	 
	 /*public ArrayList<Status> getStatusByUser(String emailID){
	        String result;
			boolean check=false;
			try{
			  while(check!=true){
				  System.out.println("trying connection in getStatusByUser");
				 check= db.start();
			  }
			  String sql="select * from status where emailID= ?";
			 
			  PreparedStatement pstmnt=db.con.prepareStatement(sql);
			  pstmnt.setString(1,emailID); // user_id is the one sent in paramater
			  ResultSet rs= pstmnt.executeQuery();
			  ArrayList<Status> status_list= new ArrayList<Status>();
			  if(rs!=null){
				  
				  while (rs.next()) {
						Status status_obj = new Status();
						status_obj.setStatusID(rs.getInt(1));
						status_obj.setStatus_desc(rs.getString(2));
						status_obj.setCreated(String.valueOf(rs.getTimestamp(3)));
						status_obj.setEmailID(emailID);
						status_obj.setFlag(rs.getInt(5));
						status_list.add(status_obj);	
					}
			  }
			  else{
				  System.out.println("resultset empty");
			  }
			  db.stop();
			  return status_list;
			}
			catch(Exception e){
				
			}
			
			 return  null;
		 }//method getStatusByUser ends here*/
		 
		 
	 
	 //this method is VERY IMPORTANT
	 //given an email ID ..it returns all status of that guy and all status of his friends
	 //along with all comments and likes on every status
	 public ArrayList<Status> getAllDetailsOfEachStatus(String emailID)
		{

		 System.out.println("eamil ddd  "+emailID);
			String rs1;
			boolean check=false;
		    

		    try{
				  while(check!=true){
					  System.out.println("trying connection in getStatus");
					 check= db.start();
				  }
		    
				  String query1="select * from status where emailID= ?";	   
				  PreparedStatement pstmnt=db.con.prepareStatement(query1);
				  pstmnt.setString(1,emailID); // user_id is the one sent in paramater
				  ResultSet rs= pstmnt.executeQuery();
				  ArrayList<Status> statusArrayList = new ArrayList<Status>();


				  
				  
				  
				  
				  
				  
				  
				  
		        while (rs.next())
		        {   System.out.println("inside first result set");
		        	Status status_obj = new Status();
					status_obj.setStatusID(rs.getInt(1));
					status_obj.setStatus_desc(rs.getString(2));
					status_obj.setCreated(String.valueOf(rs.getTimestamp(3)));
					status_obj.setEmailID(emailID);
					status_obj.setFlag(rs.getInt(5));
					//statusArrayList.add(status_obj);	

		         
					  String query11="select fname,lname from User where emailID=?";	   
					  PreparedStatement pstmnt11=db.con.prepareStatement(query11);
					  pstmnt11.setString(1,emailID); // user_id is the one sent in paramater
					  ResultSet rs11= pstmnt11.executeQuery();
					  rs11.next();
					  status_obj.setName(rs11.getString("fname")+" "+ rs11.getString("lname"));
					System.out.println("name =  "+status_obj.getName());
					
					
					
		            ArrayList<Comment> commentArrayList = new ArrayList<Comment>();
		            Comment comment_obj;
	                int stID=rs.getInt(1);
	                System.out.println("*****"+stID+"***");
	                
	                /**********LIKES************************/
		            String query3="select count(likeID) from likes where statusID=?"; 
					  PreparedStatement pstmnt3=db.con.prepareStatement(query3);
					  pstmnt3.setInt(1,stID); // user_id is the one sent in paramater
					  ResultSet rs3= pstmnt3.executeQuery();
					  
		             while(rs3.next())
		             {   System.out.println("Inside rs3. while ");
		            	 status_obj.setLikesCount(rs3.getInt(1)); 
		            	 System.out.println("likes............: "+status_obj.getLikesCount());
		             }

		             
	                /*********Comments************/
		            String query2 = "select * from comments where statusID = ?";

  		            PreparedStatement pstmnt2=db.con.prepareStatement(query2);
					 pstmnt2.setInt(1,stID); // user_id is the one sent in paramater
					 ResultSet rs2= pstmnt2.executeQuery();
					 rs2.last();
					 int rows = rs2.getRow();
					 rs2.beforeFirst();
					 System.out.println("row count:"+rows);
 	
		            while (rs2.next())
		            {   
		            	comment_obj=new Comment();
		            	System.out.println("inside second result set");
		                
		                comment_obj.setCommentID(rs2.getInt(1));
		                comment_obj.setEmailID(rs2.getString(3));
						comment_obj.setComment_desc(rs2.getString(4));
						comment_obj.setFlag(rs2.getInt(5));
		                   
		                commentArrayList.add(comment_obj);   
		                for(Comment clist:commentArrayList){
		                	System.out.print("comment list contains "+clist.getComment_desc()+" "+clist.getCommentID()+",");
		                }
		                System.out.println();
		            }//inner while ends
		           
	                System.out.println("Caling comment class method");
		            status_obj.setA(commentArrayList);		          
		            statusArrayList.add(status_obj);    
		        }//outer while ends
		        db.stop();
		        
		        java.util.Iterator<Status> itr=statusArrayList.iterator();  
		        while(itr.hasNext()){  
		        	System.out.println("inside iterator");
		         System.out.println(itr.next());  
		        }
		        return statusArrayList;
		    } 
		    catch (SQLException e) 
		    {
		        e.printStackTrace();
		    }
		   

		    return null;    
		}//method getAllDetailsOfEachStatus ends here
	 
	 
	 
	
}//class ends
