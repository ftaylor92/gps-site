package com.fmt.gps.geocode;

public class GeoCodeResult {

	//public int streetNumber;
	//public String postalcode;
	public String placename;
	//public String adminCode1;
	//public String adminName1;
	public String street;
	public String formattedAddress;
	//public String lat,lng;
	public String type;
	public float rating;

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("Type:");

		/*if (streetNumber!=null)
            builder.append(streetNumber);
        if (street!=null)
            builder.append(" "+street);
        if (placename!=null)
            builder.append(", "+placename);*/
		if (type!=null)
			builder.append(", "+type);
		builder.append(", rating:"+rating);
		builder.append(", formattedAddress:"+formattedAddress);

		return builder.toString();

	}

}
