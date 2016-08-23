# ACC1.0
This Application is designed using producer-consumer paradigm. The accelerometer data, time and activity type are collected in the main thread, then inserted into a blocking queue. In another thread, data will be stored as text file to the disk. The application will collect and store accelerometer at highest frequency.

You can find the file at sdcard/AccData. The file name is the date and time that you start to record accelerometer data. 
The format of the file name is: 
dd MM yyyy, HH:mm
The format of the output data is: 
Time(in millisecond);accelerometer data x; accelerometer data y; accelerometer data z; activity label


