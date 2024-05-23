����   < 
      java/lang/Object <init> ()V	  	 
   java/lang/System out Ljava/io/PrintStream;
      java/io/PrintStream println  CWelcome to the COVID-19 Vaccine Reservation Scheduling Application!
     (Ljava/lang/String;)V  2*** Please enter one of the following commands ***  &> create_patient <username> <password>  (> create_caregiver <username> <password>  %> login_patient <username> <password>   '> login_caregiver <username> <password> " "> search_caregiver_schedule <date> $ > reserve <date> <vaccine> & > upload_availability <date> ( > cancel <appointment_id> * > add_doses <vaccine> <number> , > show_appointments . > logout 0 > quit 2 java/io/BufferedReader 4 java/io/InputStreamReader	  6 7 8 in Ljava/io/InputStream;
 3 :  ; (Ljava/io/InputStream;)V
 1 =  > (Ljava/io/Reader;)V @ > 
  B C  print E  
 1 G H I readLine ()Ljava/lang/String; K java/io/IOException M Please try again! O  
 Q R S T U java/lang/String split '(Ljava/lang/String;)[Ljava/lang/String; W create_patient
 Q Y Z [ equals (Ljava/lang/Object;)Z
 ] ^ _ ` a scheduler/Scheduler createPatient ([Ljava/lang/String;)V c create_caregiver
 ] e f a createCaregiver h login_patient
 ] j k a loginPatient m login_caregiver
 ] o p a loginCaregiver r search_caregiver_schedule
 ] t u a searchCaregiverSchedule w reserve
 ] y w a { upload_availability
 ] } ~ a uploadAvailability � cancel
 ] � � a � 	add_doses
 ] � � a addDoses � show_appointments
 ] � � a showAppointments � logout
 ] � � a � quit � Bye! � Invalid operation name! � Failed to create user.
 ] � � � usernameExistsPatient (Ljava/lang/String;)Z � Username taken, try again!
 ] � � � strongPassword � "Please create a stronger password!
 � � � � � scheduler/util/Util generateSalt ()[B
 � � � � generateHash (Ljava/lang/String;[B)[B � &scheduler/model/Patient$PatientBuilder
 � �  � (Ljava/lang/String;[B[B)V
 � � � � build ()Lscheduler/model/Patient;
 � � � �  scheduler/model/Patient saveToDB   � � � makeConcatWithConstants &(Ljava/lang/String;)Ljava/lang/String; � java/sql/SQLException
 � � �  printStackTrace � scheduler/db/ConnectionManager
 � 
 � � � � createConnection ()Ljava/sql/Connection; � )SELECT * FROM Patients WHERE Username = ? � � � � � java/sql/Connection prepareStatement 0(Ljava/lang/String;)Ljava/sql/PreparedStatement; � � � � � java/sql/PreparedStatement 	setString (ILjava/lang/String;)V � � � � executeQuery ()Ljava/sql/ResultSet; � � � � � java/sql/ResultSet isBeforeFirst ()Z
 � � �  closeConnection � %Error occurred when checking username
 ] � � � usernameExistsCaregiver � *scheduler/model/Caregiver$CaregiverBuilder
 � �
 � � � � ()Lscheduler/model/Caregiver;
 � � � scheduler/model/Caregiver � +SELECT * FROM Caregivers WHERE Username = ?
 Q � � � length ()I � 'Password must be at least 8 characters!
 Q � � I toUpperCase
 Q I toLowerCase CPassword must be a mixture of both uppercase and lowercase letters! .*[a-zA-Z].*
 Q	 � matches .*\d.* 2Password must be a mixture of letters and numbers! 
.*[!@#?].* ]Password must include at least one special character, from “!”, “@”, “#”, “?”	 ] currentPatient Lscheduler/model/Patient;	 ] currentCaregiver Lscheduler/model/Caregiver; User already logged in Login failed. %scheduler/model/Patient$PatientGetter
! " '(Ljava/lang/String;Ljava/lang/String;)V
$% � get  �( User already logged in.* )scheduler/model/Caregiver$CaregiverGetter
)!
)-% �/ Please login first!
12345 java/sql/Date valueOf #(Ljava/lang/String;)Ljava/sql/Date;7 �SELECT C.Username, V.name, V.doses FROM Caregivers C, Availabilities A, Vaccines V WHERE A.Username = C.Username AND A.Time = ? ORDER BY C.Username �9:; setDate (ILjava/sql/Date;)V �=> � next@ Username �BC � 	getStringE NameG Doses �IJK getInt (Ljava/lang/String;)I M �N 9(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;P No availability on this date!R "java/lang/IllegalArgumentExceptionT Please enter a valid date!V !Error when searching for scheduleX Please login as a patient!Z DSELECT Username FROM Availabilities WHERE Time = ? ORDER BY Username\ No Caregiver is available!^ 2Error occurred while checking available caregivers` /SELECT Name, Doses FROM Vaccines WHERE Name = ?b Not enough available doses!d 6Error occured while checking vaccine dose availabilityf ,SELECT MAX(appointment_id) FROM Appointments �hJi (I)Ik .Error occured while getting the appointment_idm /INSERT INTO Appointments VALUES (?, ?, ?, ?, ?) �opq setInt (II)V
 �st I getUsername �vw � executeUpdatey )Error occured while inserting appointment{ :DELETE FROM Availabilities WHERE Time = ? AND Username = ?} 3Error occured while updating caregiver availability 4UPDATE Vaccines SET Doses = Doses - 1 WHERE Name = ? � �� '(ILjava/lang/String;)Ljava/lang/String;� "Please login as a caregiver first!
 �� ~� (Ljava/sql/Date;)V� Availability uploaded!� *Error occurred when uploading availability
����K java/lang/Integer parseInt� %scheduler/model/Vaccine$VaccineGetter
��  
��%� ()Lscheduler/model/Vaccine;�  Error occurred when adding doses� &scheduler/model/Vaccine$VaccineBuilder
�� � (Ljava/lang/String;I)V
�� ��
� �� scheduler/model/Vaccine
���� increaseAvailableDoses (I)V� Doses updated!� zSELECT appointment_id, vaccine_name, Time, patient_name FROM Appointments WHERE caregiver_name = ? ORDER BY appointment_id� zSELECT appointment_id, vaccine_name, Time, caregiver_name FROM Appointments WHERE patient_name = ? ORDER BY appointment_id
 �s� appointment_id� vaccine_name� Time ���5 getDate� patient_name � �� H(ILjava/lang/String;Ljava/sql/Date;Ljava/lang/String;)Ljava/lang/String;� caregiver_name �� Successfully logged out! Code LineNumberTable LocalVariableTable this Lscheduler/Scheduler; main e Ljava/io/IOException; response Ljava/lang/String; tokens [Ljava/lang/String; 	operation args r Ljava/io/BufferedReader; StackMapTable� patient Ljava/sql/SQLException; username password salt [B hash� 	statement Ljava/sql/PreparedStatement; 	resultSet Ljava/sql/ResultSet; cm  Lscheduler/db/ConnectionManager; con Ljava/sql/Connection; selectUsername� java/lang/Throwable 	caregiver vaccineName availableDoses I d Ljava/sql/Date; caregiverSchedule $Ljava/lang/IllegalArgumentException; date availableCaregiver assignedCaregiver availableVaccine 
current_id insertAppointment deleteAvailability decreaseDoses doses vaccine Lscheduler/model/Vaccine; query patientUsername caregiverUsername appointmentId <clinit> 
SourceFile Scheduler.java BootstrapMethods
 � $java/lang/invoke/StringConcatFactory �(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;	 Created user  Logged in as:   Caregiver:  Vaccine:  Doses:  (Appointment ID: , Caregiver username:  <Appointment ID: , Vaccine Name: , Date: , Patient Name:  >Appointment ID: , Vaccine Name: , Date: , Caregiver Name:  InnerClasses PatientBuilder CaregiverBuilder PatientGetter CaregiverGetter VaccineGetter VaccineBuilder %java/lang/invoke/MethodHandles$Lookup java/lang/invoke/MethodHandles Lookup ! ]     
   
       �   /     *� �   �       �       ��   	� a �      �� � � � � � � � � � � � � � � !� � #� � %� � '� � )� � +� � -� � /� � � � 1Y� 3Y� 5� 9� <L� ?� ADM+� FM� N� L� ,N� PN-�� � L� ���-2:V� X� 
-� \� �b� X� 
-� d� �g� X� 
-� i� �l� X� 
-� n� �q� X� 
-� s� �v� X� 
-� x� sz� X� 
-� |� b� X� 
-� �� Q�� X� 
-� �� @�� X� 
-� �� /�� X� 
-� �� �� X� � �� �� �� ���  � � � J �   � 7             & ! . " 6 # > $ F % N & V ' ^ ( f ) n * v + | . � 0 � 1 � 3 � 6 � 4 � 5 � 8 � : � ; � < � ? � @ � A � B � C � D � E � F G
 H I J% K, L6 M= NG ON PX Q_ Ri Sp Tz U� V� W� X� Z� \�   >  � ��  ���  � ���  � ���   ���   ��� �   6 � � 1�  � 1 Q  J� ��  Q�  
 ` a �  a     v*�� � �� �*2L*2M+� �� � �� �,� �� � �� �� �N,-� �:� �Y+-� �� �:� �� +� �  � � :� �� � ±  B c f � �   V    c  d  e  g  h  j  k & l ' n . o 6 p 7 r ; s B v R x W y c } f z h { p | u ~�   H  R �  h ��    v��    c��   _��  ; ;��  B 4�� �   ' �  Q Q� . � Q Q��  � 
 � � �  E     \� �Y� �L+� �M�N,-� � :*� � � � :� � 6+� ��:� � � �+� � :+� ���   4 ; �  4 Q   ; J Q   Q S Q   �   F    �  �  �  �  � " � + � 4 � 8 � ; � = � E � J � N � Q � W � Z ��   H   "��  + ��  = ��    \��    T��   O��   L�� �    � ;  Q � � Q  �U� 
 f a �  a     v*�� � �� �*2L*2M+� � � �� �,� �� � �� �� �N,-� �:� �Y+-� � �:� � +� �  � � :� �� � ±  B c f � �   V    �  �  �  �  �  �  � & � ' � . � 6 � 7 � ; � B � R � W � c � f � h � p � u ��   H  R �  h ��    v��    c��   _��  ; ;��  B 4�� �   ' �  Q Q� . � Q Q��  � 
 � � �  E     \� �Y� �L+� �M�N,-� � :*� � � � :� � 6+� ��:� � � �+� � :+� ���   4 ; �  4 Q   ; J Q   Q S Q   �   F    �  �  �  �  � " � + � 4 � 8 � ; � = � E � J � N � Q � W � Z ��   H   "��  + ��  = ��    \��    T��   O��   L�� �    � ;  Q � � Q  �U� 
 � � �   �     d*� �� � �� �**� �� **� � � � �*�� *
�� � � �*�� � � ��   �   6    � 	 �  �  � # � , � . � B � K � M � W � ` � b ��       d��  �    
 
 k a �  7     q�� 	�� � � �*�� � � �*2L*2MN�Y+,� �#N� :� � � �-� � � � � +�&  � -��  0 = @ � �   N    �  �  �  �  � % � & � * � . � 0 � = � @ � B � K � P � T � ` � l � p ��   4  B ��    q��   * G��  . C��  0 A� �    	�  � Q Q �  � 
 p a �  7     q�� 	�� � '� �*�� � � �*2L*2MN�)Y+,�+�,N� :� � � �-� � � � � +�&  � -��  0 = @ � �   N       	 %
 & * . 0 = @ B K P T ` l p�   4  B ��    q��   * G��  . C��  0 A� �    	�  � Q Q �  � 
 u a �  V     ڲ� �� � .� �*�� � L� �*2L� �Y� �M,� �NN:+�0:6:-� � :�8 � � :�< � ;?�A :D�A :	F�H 6
� 	
�L  � ���N� X� � O� �� !:� S� � :� U� � ±  : � �Q : � � � �   �    # $ % ( ) $* %, )- 1. 6/ :1 @2 E5 O6 Y7 b8 l9 x: �; �< �> �? �@ �A �H �C �D �H �E �F �G �I�   �  � �� 	 � �� 
 @ x��  E s��  O i��  b V��  � 	��  � ��    ���   ) ���  1 ���  6 ���  : ��� �   H � < 	� Q � � Q1 Q � �  � A�  � Q � � Q  BQM � 
 w a �  �    m�� �� � .� ��� � W� �*�� � L� �*2L� �Y� �M,� �NY:N:+�0:-� � :�8 � � :�< � ?�A ::N� X� � [� �� !:� ]� � § :� S� *2:6_:-� � :		� � 	� � :

�< � 
F�H 6� � a� �� :	� c� 	� �e:	6
-	� � :� � :�< � �g `� 6
� :� j� � �l:+�0:-� � :
�n �8 � � ��r� � � � �u W� :� x� � �z:+�0:-� � :�8 � � �u W� :� |� � �~:-� � :� � �u W,� � #:� |� � �,� � :,� ��� 
��  � � 
 O � � � O � �Q � �+X[ �p�� �� �6= �6T  =MT  TVT   �  v ]  O P Q T U %V &Y ,Z 4[ 5] 9^ A_ F` Kb Od Ue _f ig rh |i �j �l �m �n �u �p �q �r �u �s �t �v �w �x �z �{ �| �} �~�������#�(�+�5�>�X�[�]�f�k�p�v���������������������������������������$�.�6�:�=�?�H�M�Q�T�Z�]�l��  L ! U K��  _ A��  r .��  � ��  � 	��  � 8�� 	 � %�� 
 �� 	5 #�� > �� ] �� v H�� � >�� � �� � &�� � ��  �� $ �� ? ��   m��   94��  A,��  F'��  K"��  O��  ����  ����  ���� (E�� 	+B�� 
p ��� � ���  S�� �  C � V 	� Q � � Q Q1 � �  � B �RQ
� ? � Q � � Q Q Q Q � �  � B �� 1 � Q � � Q Q Q Q Q � �  @�  � Q � � Q Q Q Q Q  �� U � Q � � Q Q Q Q Q Q  �� 3 � Q � � Q Q Q Q Q Q Q  �� ' � Q � � Q Q Q Q Q Q Q Q  �V� 
 ~ a �       W�� � �� �*�� � L� �*2L+�0M�,��� �� � M� S� � M� �� ,� ±  # 8 ;Q # 8 H � �   J   � � � � � � � #� (� /� 8� ;� <� E� H� I� R� V��   4  ( ��  < 	��  I ��    W��   # 4�� �    �  � Q QL � 
 � a �   +      �   �      ��       ��   
 � a �  �     ��� � �� �*�� � L� �*2L*2��=N��Y+����N� :� �� � �-� *��Y+����N-��� .:� �� � § -��� :� �� � ² �� �  , 8 ; � O ` c � v { ~ � �   v   � � � � � � � #� *� ,� 8� ;� =� F� K� O� \� `� c� e� n� s� v� { ~� �  � � ��   H  = ��  e ��  � ��    ���   # u��  * n��  , l�� �   # �  � Q�  �W �G � 
 � a �  �  
   ��� �� � .� �*�� � L� ��� 
�L� �L� �Y� �M,� �N-+� � :�� ���� 	��r� � � � :�< � q��H 6��A :��� :�� %��A :	� 	��  � � "��A :	� 	��  � ���� L� L� +� ±  % � � � �   �         $ % + 2 6 > C  L! ^" d! i# r$ |% �& �' �( �) �* �. �/ �0 �5 �9 �6 �7 �8 �:�   �  / ��  � �� 	 � �� 	 � _��  � S��  � G��  6 ���  > ���  C ���  L ���  r x��  � ��    ���  �   d �  Q� ' � Q � � �  ��  � Q � � �  � Q�  �� U Q1� �  �  B � 
 � a �   �     7�� �� � .� �*�� � L� ���� �� �   �   * 
  @ A B E F $G %J )K -L 6M�       7��  �     �  �   %      	���   �   
       �       &   
       :  � � 	 � � 	 � 	) � 	�� 	�� 	 