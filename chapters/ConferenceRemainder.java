package com.sap.datetime.remainder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;


public class ConferenceRemainder  {	
   	  
   /**
	* Date of the technical conference   
    */
	private ZonedDateTime conferenceDateAndTime ;
		
	private ZonedDateTime startMeetingDateAndTime ;
	
	int startMeetingHours , startMeetingMinutes;
		
	private ZoneId zoneIdOfCurrentLocation;
	
	
	
	/**
	 *  Initialize the conference remainder with information about the conference start date and time, zone id of the team's location and 
	 *  the start meeting time.
	 * @param zoneIdOfCurrentLocation - zoneId of the team location
	 * @param startMeetingHours 
	 * @param startMeetingMinutes
	 */
	public ConferenceRemainder( ZoneId zoneIdOfCurrentLocation ,int startMeetingHours , int startMeetingMinutes ){
			this.zoneIdOfCurrentLocation = zoneIdOfCurrentLocation;
			conferenceDateAndTime = ZonedDateTime.of(2015, 6, 20, 11, 15, 00, 00, zoneIdOfCurrentLocation);	
			this.startMeetingHours = startMeetingHours;
			this.startMeetingMinutes = startMeetingMinutes;
			
	}
	
	
	
	
	/**
	 * Initialize start meeting date time to the current Monday ( if today is Monday, before the meeting time ) or to the next Monday if the meeting time for 
	 * today is  already gone. 
	 */
	private void initStartMeetingDateAndTime() {
		ZonedDateTime monday= null;
		// check if today is Monday 
		if(DayOfWeek.MONDAY.getValue() == currentDateAndTime.get(ChronoField.DAY_OF_WEEK) &&
			currentDateAndTime.get(ChronoField.HOUR_OF_DAY) <= startMeetingHours && 
			currentDateAndTime.get(ChronoField.MINUTE_OF_HOUR) < startMeetingMinutes ) { 
			
			monday = currentDateAndTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)); 
		}else {	
			// The meeting time is already gone. Start meeting time should be the next Monday
			monday = currentDateAndTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); 
		}
		
		
		//if the "monday" is before the conference date, then set it for start meeting date
		setStartMeetingDateAndTime( monday );
		
		System.out.println( "initStartMeetingTime: Curreint time is: " + currentDateAndTime);
		System.out.println( "startMeetingTime is: " + startMeetingDateAndTime);
		
	}

	/**
	 * Calculate the date and time for the next meeting, which is at the next Monday 
	 */
	private void setNextStartMeetingDateAndTime() {		
		//To use a TemporalAdjuster use the "with" method. This method returns an adjusted copy of the date-time. 
		ZonedDateTime nextMonday=  currentDateAndTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		//if the "nextMonday" is before the conference date, then set it for start meeting date
		setStartMeetingDateAndTime( nextMonday );		
		
		System.out.println( "****************************************************************************");
		System.out.println( "Next start meeting time is on : " + startMeetingDateAndTime.format(formatter));
		System.out.println( "****************************************************************************");
	}
	
	
	/**
	 * The remainder is active until the last Monday, before the conference data, that's why the start meeting date and time is set only if the "monday" 
	 * date is before the conference date.
	 * @param monday 
	 */
	private void setStartMeetingDateAndTime( ZonedDateTime monday ){
		// start meeting date and time is not set if date of 'moday' is after the conference date
		if( monday.isBefore(conferenceDateAndTime)) {
			startMeetingDateAndTime = ZonedDateTime.ofInstant(monday.toInstant(), currentDateAndTime.getZone()); 
			// add the start meting time to the start meeting date
			addStartMeetingTimeToStartMeetingDate ();
		}
	}
	
	/**
	 * Set start meeting time to the start meeting date
	 * @param monday
	 */
	private void addStartMeetingTimeToStartMeetingDate(){			    
		    startMeetingDateAndTime = startMeetingDateAndTime.withHour(startMeetingHours);
		    startMeetingDateAndTime = startMeetingDateAndTime.withMinute(startMeetingMinutes);		
	}	
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss VV");	
	private void showRemainder( ){
		    
		    //print the remainder message with information about the current date and time in the corresponding time zone
			System.out.println( "\nToday is : "+ currentDateAndTime.getDayOfWeek().name()+ " - " + currentDateAndTime.format(formatter) );
			System.out.println("There are 15 minutes to the meeting : ");
			
			// print details for the meeting which include the type of the meeting and start meeting time			 
			 System.out.println("This is monthly meeting remainder which is displayed every Monday fifteen minutes before: "+ 
		    		                                   startMeetingDateAndTime.format(DateTimeFormatter.ofPattern("hh:mm VV")) );			  
		    System.out.println("_________________________________________________________________________________________________\n \n");
	}
	
	
	
	
	
	
	
	
	private ZonedDateTime currentDateAndTime;	
	
	public void startRemainder(){	
		
		//Initialize the clock with the current time with the corresponding time zone of the team's location
		Clock currentTimeClock = Clock.system(zoneIdOfCurrentLocation);
		currentDateAndTime = ZonedDateTime.now( currentTimeClock );
		System.out.println( "Weekly meeting. It runs every Monday.");
		System.out.println( "Started at : " + currentDateAndTime +" "+ currentDateAndTime.getZone());
		
		// initialize the start meeting date and time with the date of the first "Monday" 
		initStartMeetingDateAndTime();
		
		boolean running = true;
		while( running ){
			//Check if the remainder is still valid; Start meeting date and time is set until the last "Monday" before the conference date
			if( currentDateAndTime.isBefore(startMeetingDateAndTime)){
				
				//check if current date and time is 15 minutes before the meeting
				if(currentDateAndTime.until(startMeetingDateAndTime, ChronoUnit.MINUTES) == 15 ){
					showRemainder( );
					setNextStartMeetingDateAndTime();
				}
			
			//Stop the remainder, the last "Monday" before the conference date is reached
			}else{				
				
				System.out.println( "The conference date is on " + conferenceDateAndTime.format(formatter) );
				System.out.println( "Now is : "+ currentDateAndTime.format(formatter)  );
				System.out.println( "The last meeting date before the conference is reached. Remainder is stopped.");				
				running = false;
			}
			
			// Move the current time with one minutes. This simulate f time.
			currentTimeClock = Clock.fixed(currentTimeClock.instant().plus(1, ChronoUnit.MINUTES), currentDateAndTime.getZone());
			currentDateAndTime = ZonedDateTime.now( currentTimeClock );
		}
		
	}


	public static void main(String[] args) {
		ConferenceRemainder conferenceRemainderBulgaria = new ConferenceRemainder(ZoneId.of("Europe/Sofia"), 18, 30);		
		conferenceRemainderBulgaria.startRemainder();
		
		System.out.println("######################################################################################################");
		
		ConferenceRemainder conferenceRemainderAfrica = new ConferenceRemainder(ZoneId.of("Africa/Johannesburg"), 10, 30);
		conferenceRemainderAfrica.startRemainder();

	}

}
