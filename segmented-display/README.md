# segmented-display

A segmented display

This library provides the following Composable functions that can be used to create a segmented
display:
* [Rect7SegmentDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/Rect7Segment.kt)
* [Hexagonal7SegmentDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/Hexagonal7Segment.kt)

Of these, the one you'll most likely want to use is
[Hexagonal7SegmentDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/Hexagonal7Segment.kt)
because it is the most like a real segmented display.
[Rect7SegmentDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/Rect7Segment.kt), on the other
hand, is super-basic, and it only draws rectangular segments 