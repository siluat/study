import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
            floatingActionButton: FloatingActionButton(
              onPressed: () {},
              child: const Text('클릭'),
            ),
            body: SafeArea(
                top: true,
                bottom: true,
                left: true,
                right: true,
                child: ListView(children: [
                  Column(
                    children: [
                      const Text('텍스트 위젯',
                          style: TextStyle(
                              fontSize: 16.0,
                              fontWeight: FontWeight.w700,
                              color: Colors.blue)),
                      TextButton(
                          onPressed: () {},
                          style:
                              TextButton.styleFrom(foregroundColor: Colors.red),
                          child: const Text('텍스트 버튼')),
                      OutlinedButton(
                          onPressed: () {},
                          style: OutlinedButton.styleFrom(
                              foregroundColor: Colors.red),
                          child: const Text('아웃라인드 버튼')),
                      ElevatedButton(
                          onPressed: () {},
                          style: ElevatedButton.styleFrom(
                              foregroundColor: Colors.white,
                              backgroundColor: Colors.red),
                          child: const Text('엘리베이티드 버튼')),
                      IconButton(
                        onPressed: () {},
                        icon: const Icon(Icons.home),
                      ),
                      GestureDetector(
                          onTap: () {
                            // ignore: avoid_print
                            print('on tap');
                          },
                          onDoubleTap: () {
                            // ignore: avoid_print
                            print('on double tap');
                          },
                          onLongPress: () {
                            // ignore: avoid_print
                            print('on long press');
                          },
                          child: Container(
                            decoration: const BoxDecoration(color: Colors.red),
                            width: 100.0,
                            height: 100.0,
                          )),
                      Container(
                        decoration: BoxDecoration(
                            color: Colors.red,
                            border:
                                Border.all(width: 16.0, color: Colors.black),
                            borderRadius: BorderRadius.circular(16.0)),
                        height: 200.0,
                        width: 100.0,
                      ),
                      SizedBox(
                          height: 200.0,
                          width: 200.0,
                          child: Container(color: Colors.red)),
                      Container(
                          color: Colors.blue,
                          child: Padding(
                            padding: const EdgeInsets.all(16.0),
                            child: Container(
                                color: Colors.red, width: 50.0, height: 50.0),
                          )),
                      Container(
                          color: Colors.black,
                          margin: const EdgeInsets.all(16.0),
                          child: Padding(
                              padding: const EdgeInsets.all(16.0),
                              child: Container(
                                color: Colors.red,
                                width: 50,
                                height: 50,
                              ))),
                    ],
                  ),
                  SizedBox(
                      height: 200.0,
                      child: Column(children: [
                        Flexible(flex: 3, child: Container(color: Colors.blue)),
                        Flexible(flex: 1, child: Container(color: Colors.red))
                      ])),
                  SizedBox(
                      height: 200.0,
                      child: Column(children: [
                        Expanded(child: Container(color: Colors.blue)),
                        Expanded(flex: 1, child: Container(color: Colors.red))
                      ])),
                  Stack(
                    children: [
                      Container(
                        height: 300.0,
                        width: 300.0,
                        color: Colors.red,
                      ),
                      Container(
                        height: 250.0,
                        width: 250.0,
                        color: Colors.yellow,
                      ),
                      Container(
                        height: 200.0,
                        width: 200.0,
                        color: Colors.blue,
                      )
                    ],
                  )
                ]))));
  }
}
