// SPDX-License-Identifier: Apache-2.0
// -----------------------------------------------------------------------------
// Extend.scala
// -----------------------------------------------------------------------------
// Chisel implementation of the Verilog module:
//
//   module extend (
//       input  wire [31:7] instr,
//       input  wire [ 2:0] immsrc,
//       output reg  [31:0] immext
//   );
//
// that sign-/zero‑extends the various RISC‑V immediates (I, S, B, J, U).
// The upstream design presents only instr[31:7] (opcode stripped off). We model
// that exactly by making the Chisel port 25 bits wide. Bit mapping:
//
//   Orig RV32I bit -> io.instr bit
//   --------------------------------
//   31 -> 24   (sign bit)
//   30 -> 23
//   29 -> 22
//   28 -> 21
//   27 -> 20
//   26 -> 19
//   25 -> 18
//   24 -> 17
//   23 -> 16
//   22 -> 15
//   21 -> 14
//   20 -> 13
//   19 -> 12
//   18 -> 11
//   17 -> 10
//   16 ->  9
//   15 ->  8
//   14 ->  7
//   13 ->  6
//   12 ->  5
//   11 ->  4
//   10 ->  3
//    9 ->  2
//    8 ->  1
//    7 ->  0
//
// NOTE: The literal encodings used below for immsrc (0.U .. 4.U) must match the
// `IMMSRC_*` values used elsewhere in your RTL (e.g., in parameter or define
// files). Adjust if your encodings differ.
// -----------------------------------------------------------------------------

import chisel3._
import chisel3.util._
import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

/** Immediate source selector encodings.
  * Adjust the UInt literals if you need to match existing Verilog `define`s.
  */
object ImmSrc {
  val ITYPE = "b000".U(3.W)
  val STYPE = "b001".U(3.W)
  val BTYPE = "b010".U(3.W)
  val JTYPE = "b011".U(3.W)
  val UTYPE = "b100".U(3.W)
}

/** Immediate extender: instr[31:7] + immsrc -> immext[31:0]. */
class Extend extends Module {
  val io = IO(new Bundle {
    val instr  = Input(UInt(25.W)) // corresponds to instr[31:7]
    val immsrc = Input(UInt(3.W))  // ImmSrc selector
    val immext = Output(UInt(32.W))
  })

  // Convenience aliases ------------------------------------------------------
  // Sign bit (instr[31]) is io.instr(24).
  val sign = io.instr(24)

  // I-type: {{20{instr[31]}}, instr[31:20]}
  val immI = Cat(Fill(20, sign), io.instr(24,13))

  // S-type: {{20{instr[31]}}, instr[31:25], instr[11:7]}
  val immS = Cat(Fill(20, sign), io.instr(24,18), io.instr(4,0))

  // B-type: {{20{instr[31]}}, instr[7], instr[30:25], instr[11:8], 1'b0}
  val immB = Cat(Fill(20, sign), io.instr(0), io.instr(23,18), io.instr(4,1), 0.U(1.W))

  // J-type: {{12{instr[31]}}, instr[19:12], instr[20], instr[30:21], 1'b0}
  val immJ = Cat(Fill(12, sign), io.instr(12,5), io.instr(13), io.instr(23,14), 0.U(1.W))

  // U-type: {instr[31:12], 12'b0}
  val immU = Cat(io.instr(24,5), 0.U(12.W))

  // Default 0.
  io.immext := 0.U
  switch (io.immsrc) {
    is (ImmSrc.ITYPE) { io.immext := immI }
    is (ImmSrc.STYPE) { io.immext := immS }
    is (ImmSrc.BTYPE) { io.immext := immB }
    is (ImmSrc.JTYPE) { io.immext := immJ }
    is (ImmSrc.UTYPE) { io.immext := immU }
  }
}

// -----------------------------------------------------------------------------
// Elaborate / emit Verilog helper.
// Usage (sbt / mill / ammonite): runMain ExtendVerilog
// -----------------------------------------------------------------------------
object ExtendVerilog extends App {
  val verilog = getVerilogString(new Extend)
  val path = Paths.get("extend.v")
  Files.write(path, verilog.getBytes(StandardCharsets.UTF_8))
  println(s"Verilog written to ${path.toAbsolutePath}")
}

