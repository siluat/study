import 'package:calendar_scheduler/provider/schedule_provider.dart';
import 'package:calendar_scheduler/repository/schedule_repository.dart';
import 'package:calendar_scheduler/screen/home_screen.dart';
import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:provider/provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await initializeDateFormatting();

  final repository = ScheduleRepository();
  final scheduleProvider = ScheduleProvider(repository: repository);

  runApp(
    ChangeNotifierProvider(
      create: (_) => scheduleProvider,
      child: const MaterialApp(
        home: HomeScreen(),
      ),
    ),
  );
}
