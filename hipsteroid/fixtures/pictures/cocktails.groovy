import co.freeside.hipsteroid.Picture

fixture {
	manhattan Picture, image: Picture.getResource('/manhattan.jpg').bytes, uploadedBy: 61233112
	martini Picture, image: Picture.getResource('/martini.jpg').bytes, uploadedBy: 61233112
	gibson Picture, image: Picture.getResource('/gibson.jpg').bytes, uploadedBy: 61233112
	oldfashioned Picture, image: Picture.getResource('/oldfashioned.jpg').bytes, uploadedBy: 61233112
}