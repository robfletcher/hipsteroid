package co.freeside.hipsteroid.viewhelpers

import java.text.SimpleDateFormat
import com.github.jknack.handlebars.*
import static humanize.Humanize.naturalTime

class FriendlyTime implements Helper<String> {

	@Override
	CharSequence apply(String context, Options options) throws IOException {
		def date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(context)
		new Handlebars.SafeString("<time datetime=\"${context}\">${naturalTime(date)}</time>")
	}

}
