package org.mcsg.bot.skype.util;
public class Fader {

	double cur_r, cur_g, cur_b;
	double add_r, add_g, add_b;
	int cur_steps, target_steps;

	public Fader( int start_r, int start_g, int start_b,int target_r, int target_g, int target_b, int steps) {

		this.cur_r = start_r + 0.0;
		this.cur_g = start_g + 0.0;
		this.cur_b = start_b + 0.0;

		this.add_r = (target_r - start_r) / steps;
		this.add_b = (target_b - start_b) / steps;
		this.add_g = (target_g - start_g) / steps;

		this.cur_steps = 0;
		this.target_steps = steps;

	}

	public int[] step(){
		this.cur_r += add_r;
		this.cur_g += add_g;
		this.cur_b += add_b;
		
		this.cur_steps++;

		return new int[]{(int) cur_r, (int) cur_b, (int) cur_g};
	}

	public boolean hasNext(){
		return cur_steps < target_steps;
	}


}