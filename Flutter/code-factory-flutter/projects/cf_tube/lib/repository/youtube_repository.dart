import 'package:cf_tube/const/api.dart';
import 'package:cf_tube/model/video_model.dart';
import 'package:dio/dio.dart';

class YoutubeRepository {
  static Future<List<VideoModel>> getVideos() async {
    final resp = await Dio().get(
      youtubeApiBaseUrl,
      queryParameters: {
        'channelId': cfChannelId,
        'maxResults': 50,
        'key': apiKey,
        'part': 'snippet',
        'order': 'date',
      },
    );

    final listWithData = (resp.data['items'] as List).where(
      (item) =>
          item?['id']?['videoId'] != null && item?['snippet']?['title'] != null,
    );

    return listWithData
        .map(
          (item) => VideoModel(
            id: item['id']['videoId'],
            title: item['snippet']['title'],
          ),
        )
        .toList();
  }
}
