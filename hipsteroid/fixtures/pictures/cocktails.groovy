import co.freeside.hipsteroid.Picture
import co.freeside.hipsteroid.auth.User

fixture {
	def user = User.findByUsername('hipsteroid')
	manhattan Picture, image: Picture.getResource('/manhattan.jpg').bytes, uploadedBy: user
	martini Picture, image: Picture.getResource('/martini.jpg').bytes, uploadedBy: user
	gibson Picture, image: Picture.getResource('/gibson.jpg').bytes, uploadedBy: user
	oldfashioned Picture, image: Picture.getResource('/oldfashioned.jpg').bytes, uploadedBy: user
}