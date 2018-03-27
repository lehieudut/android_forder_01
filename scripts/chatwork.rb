unless ARGV[0].empty?
	require "chatwork"

	# Create message
	ChatWork.api_key = ""
	ChatWork::Message.create(room_id: 69661066, body: "[info][To:2391265] [To:2391270] \n\n CI Build Success! \n\n Please Check! \n Link: #{ARGV[0]} [/info]")
end
