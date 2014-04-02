package processor.support;
import javax.sound.midi.ShortMessage;

/**
 * Wraps logic around a ShortMessage
 * @author Steve
 *
 */
public class ShortMessageParser 
{
	private ShortMessage shortMessage;
	
	/**
	 * MIDI event for when a note triggers on
	 */
	public static final int NOTE_ON = 0x90;
	
	/**
	 * MIDI event for when a note triggers off
	 */
	public static final int NOTE_OFF = 0x80;
	
	/**
	 * Create a new ShortMessageParser
	 * @param sm The ShortMessage to parse
	 */
	public ShortMessageParser(ShortMessage sm)
	{
		this.shortMessage = sm;
	}
	
	/**
	 * 
	 * @return Was this message an on message
	 */
	public boolean isOnMessage()
	{
		return this.shortMessage.getCommand() == NOTE_ON;
	}
	
	/**
	 * 
	 * @return Was this message an off message
	 */
	public boolean isOffMessage()
	{
		return this.shortMessage.getCommand() == NOTE_OFF;
	}
	
	/**
	 * 
	 * @return The integer note value
	 */
	public int getLastPitch()
	{
		return this.shortMessage.getData1();
	}
}
