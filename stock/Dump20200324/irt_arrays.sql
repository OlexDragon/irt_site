-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 192.168.2.241    Database: irt
-- ------------------------------------------------------
-- Server version	5.5.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `arrays`
--

DROP TABLE IF EXISTS `arrays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `arrays` (
  `name` varchar(20) NOT NULL,
  `id` varchar(20) NOT NULL,
  `description` varchar(45) NOT NULL,
  `sequence` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`name`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arrays`
--

LOCK TABLES `arrays` WRITE;
/*!40000 ALTER TABLE `arrays` DISABLE KEYS */;
INSERT INTO `arrays` VALUES ('ampl_titles','<br />Leads Number','text',4),('ampl_titles','Description','text',5),('ampl_titles','Mfr','select',2),('ampl_titles','Mfr P/N','text',1),('ampl_titles','Package','select',3),('ampl_titles','SeqN','label',6),('assemblied','TPA','SSPA',NULL),('assemblied','TPB','SSPB',NULL),('assem_titles','Description','text',4),('assem_titles','Option','select',2),('assem_titles','Revision','select',3),('assem_titles','Top Level','select',1),('assy_titles','Description','text',4),('assy_titles','Option','select',2),('assy_titles','Revision','select',3),('assy_titles','SeqN','label',1),('band','CB0','C - (5.85 - 6.425 GHz)',NULL),('band','CB1','C - (4.4 - 5.0 GHz)',NULL),('band','CSE','C - (5.8 - 7.1 GHz)',NULL),('band','CXB','C - (5.85 - 6.725 GHz)',NULL),('band','KBB','KU - (12.7 - 13.2 GHz)',NULL),('band','KUB','KU - (14 - 14.5 GHz)',NULL),('band','KUL','KU - (10.9 - 12.8 GHz)',NULL),('band','KXB','KU - (13.75 - 14.5 GHz)',NULL),('band','KXW','KU Wide (10 - 15 GHz)',NULL),('band','RCB','C - (3.4 - 4.2 GHz)',NULL),('band','RSB','S - (2.025 - 2.4 GHz)',NULL),('band','RXB','X - (7.2 - 7.8 GHz)',NULL),('band','SBD','S - (2 - 2.4 GHz)',NULL),('band','XBD','X - (7.7 - 8.5 GHz)',NULL),('band_buc','LCB','L to CB - (5.85 - 6.425)',NULL),('band_buc','LKU','L to KU - (14 - 14.5 GHz)',NULL),('band_buc','LKX','L to KU - (13.75 - 14.5 GHz)',NULL),('band_dc','L14','L - (0.95 - 2.15 GHz)  to 140 MHz',NULL),('band_dc','LX7','L - (0.95 - 2.15 GHz) to 70 MHz',NULL),('band_uc','14X','140 MHz to L - (0.95 - 2.15 GHz)',NULL),('band_uc','7LX','70 MHz to L - (0.95 - 2.15 GHz)',NULL),('board_mat','8H','FR408HR',NULL),('board_mat','PT','PTFE',NULL),('board_mat','RG','Rogers',NULL),('board_title','Description','text',4),('board_title','Material','select',1),('board_title','Revision','select',3),('board_title','Type','select',2),('board_type','BIAS','BIAS',NULL),('board_type','CBCV','C-Band Converter',NULL),('board_type','CNTR','Controller',NULL),('board_type','CONV','Converter',NULL),('board_type','GEN0','General',NULL),('board_type','IM00','Interconnection Module',NULL),('board_type','IM01','Interconnection Module 01',NULL),('board_type','IMO1','Input Module Option 1',NULL),('board_type','IMO2','Input Module Option 2',NULL),('board_type','INPT','Input Module',NULL),('board_type','LNKU','LNB KU',NULL),('board_type','MP00','Middle Plane',NULL),('board_type','PS00','PS',NULL),('board_type','PS01','PS Option 1',NULL),('board_type','PS02','PS Option 2',NULL),('board_type','PS03','PS Option 3',NULL),('board_type','PS04','PS Option 4',NULL),('board_type','PS12','PS 12V',NULL),('board_type','PS28','PS 28V',NULL),('board_type','PSEB','PS Extension',NULL),('board_type','RFB2','RF Board op.2',NULL),('board_type','RFBC','RF Board C-Band',NULL),('board_type','RFBD','RF Board op.1',NULL),('board_type','XBCV','X-Band Converter',NULL),('bom','PCA','PCB',NULL),('bom','TFC','TOP',NULL),('bom','TPA','TOP',NULL),('bom','TPB','TOP',NULL),('bom','TRS','TOP',NULL),('cable_titles','<br />Description','text',6),('cable_titles','Cable type','select',3),('cable_titles','Con1 Type','select',1),('cable_titles','Con2 Type','select',2),('cable_titles','Length(cm)','text',4),('cable_titles','Mfr','select',7),('cable_titles','Mfr P/N','text',8),('cable_titles','SeqN','label',5),('cable_type','12','12 Wires',NULL),('cable_type','14','14 Wires',NULL),('cable_type','20','IDC-20',NULL),('cable_type','2R','20 Ribbon ',NULL),('cable_type','3W','03 Wires',NULL),('cable_type','40','IDC-40',NULL),('cable_type','8W','8 Wires',NULL),('cable_type','C5','COAX 50',NULL),('cable_type','C7','COAX 75',NULL),('cable_type','FL','COAX 316 (FLEX)',NULL),('cable_type','ID','IDC-14',NULL),('cable_type','OT','Other',NULL),('cable_type','PC','Power Cord',NULL),('cable_type','SF','Semiflex',NULL),('cable_type','SR','Semirigid',NULL),('cable_type','T5','CAT5',NULL),('cab_con_type','19','C19',NULL),('cab_con_type','AC','AC Power',NULL),('cab_con_type','BF','BNC (FEMALE)',NULL),('cab_con_type','BM','BNC (MALE)',NULL),('cab_con_type','CR','Circular',NULL),('cab_con_type','DU','Duo-Clasp',NULL),('cab_con_type','FF','FFSD',NULL),('cab_con_type','FM','F-Type MALE',NULL),('cab_con_type','ID','IDSD',NULL),('cab_con_type','M9','D-SUB 9 pins male',NULL),('cab_con_type','ML','Military',NULL),('cab_con_type','NB','N-Type BULKHEAD',NULL),('cab_con_type','NE','NEMA 5-15',NULL),('cab_con_type','NF','N-Type FEMALE',NULL),('cab_con_type','NM','N-Type MALE',NULL),('cab_con_type','OP','Open',NULL),('cab_con_type','OT','Other',NULL),('cab_con_type','RJ','RJ45',NULL),('cab_con_type','SA','SMA STRAIGHT',NULL),('cab_con_type','SB','SMA BULKHEAD',NULL),('cab_con_type','SC','Socket',NULL),('cab_con_type','SP','SMP',NULL),('cab_con_type','SR','SMA RIGHT ANGLE',NULL),('cap_mounting','S','SMT',NULL),('cap_mounting','T','Thru Hole',NULL),('cap_titles','<br />Description','text',6),('cap_titles','Mfr','select',7),('cap_titles','Mfr P/N','text',8),('cap_titles','Mounting','select',3),('cap_titles','Size','select',5),('cap_titles','Type','select',2),('cap_titles','Value(pF)','text',1),('cap_titles','Voltage','text',4),('cap_type','C','Ceramic',NULL),('cap_type','E','Electrolitic',NULL),('cap_type','F','Film',NULL),('cap_type','P','NP0/C0G',NULL),('cap_type','R','Trimmer',NULL),('cap_type','T','Tantalum',NULL),('comp_titles','Description','text',1),('comp_titles','Mfr','select',2),('comp_titles','Mfr P/N','text',3),('con_titles',' Numb.of pin','text',2),('con_titles',' Seq.Num','label',5),('con_titles','<br />Description','text',6),('con_titles','M/F','select',3),('con_titles','Mfr','select',7),('con_titles','Mfr P/N','text',1),('con_titles','Type','select',4),('con_type','19','C19',NULL),('con_type','CT','Crimp Terminals ',NULL),('con_type','FT','F-Type',NULL),('con_type','MI','Military',NULL),('con_type','NE','NEMA 5-15',NULL),('con_type','NP','Panel Mount',NULL),('con_type','S2','SMA 2 Holes',NULL),('con_type','S4','SMA 4 Holes',NULL),('con_type','SC','SMT',NULL),('con_type','SM','SMP/SMT',NULL),('con_type','ST','SMP/Thru Hole',NULL),('con_type','TH','Thru Hole',NULL),('con_type','TS','SMA/Thru Hole',NULL),('con_type','WB','Wire to Board',NULL),('cost_class','PCA','Assembled PCB',NULL),('cost_class','T','Top Level',NULL),('details','DC07','L to 70',NULL),('details','DC14','L to 140',NULL),('details','DCKU','Ku to L',NULL),('details','DCLB','C to L',NULL),('details','R100','Ref.100 MHz',NULL),('details','R10M','Ref.10 MHz',NULL),('details','U14C','140 to C',NULL),('details','U14K','140  to Ku',NULL),('details','U70C','70  to C',NULL),('details','U70K','70  to KU',NULL),('details','UC07','70  to L',NULL),('details','UC14','140  to L',NULL),('details','UCCB','L to C',NULL),('details','UCKA','L to Ka',NULL),('details','UCKU','L to Ku',NULL),('diode_type','LE','LED',NULL),('diode_type','SC','Schottky',NULL),('diode_type','SW','Switching Diode',NULL),('diode_type','TU','Tunnel',NULL),('diode_type','TV','TVC',NULL),('diode_type','ZE','Zener',NULL),('fet_package','12','12F',NULL),('fet_package','21','2-16G1B',NULL),('fet_package','69','GF-69',NULL),('fet_package','73','7-AA03A',NULL),('fet_package','74','7-AA04A',NULL),('fet_package','76','7-AA06A',NULL),('fet_package','77','7-AA07A',NULL),('fet_package','79','7-AA09A',NULL),('fet_package','7E','7-BA15A',NULL),('fet_package','IA','2-9D1B / IA',NULL),('fet_package','IB','2-11D1B / IB / GF-68',NULL),('fet_titles','<br />Description','text',5),('fet_titles','Band','select',1),('fet_titles','Mfr','select',6),('fet_titles','Mfr P/N','text',7),('fet_titles','Package','select',4),('fet_titles','Power(Watt)','text',2),('fet_titles','Type','select',3),('fet_type','GA','GaAs',NULL),('fet_type','GN','GaN',NULL),('from_to','1','Stock',NULL),('from_to','3','Assembled',NULL),('from_to','4','CM',NULL),('from_to','5','Vendor',NULL),('from_to','6','KIT',NULL),('from_to','7','Bulk',NULL),('gasket_type','FG','Flange Gasket',NULL),('gasket_type','OR','O-Ring',NULL),('gasket_type','OT','Other',NULL),('gasket_type','WG','Waveguide Gaskets',NULL),('gskt_titles','<br />Description','text',4),('gskt_titles','Diameter','text',2),('gskt_titles','Mfr','select',5),('gskt_titles','Mfr P/N','text',6),('gskt_titles','SeqN','label',3),('gskt_titles','Type','select',1),('history_oper','1','Insert',NULL),('history_oper','2','Update',NULL),('ic_package','DF','DFN',NULL),('ic_package','DU','DCU',NULL),('ic_package','FP','QFP',NULL),('ic_package','FT','Feed-Through',NULL),('ic_package','MS','MSOP',NULL),('ic_package','OT','Other',NULL),('ic_package','QF','QFN',NULL),('ic_package','SM','SMT',NULL),('ic_package','SN','SON',NULL),('ic_package','ST','SOT',NULL),('ic_package','TH','Thru Hole',NULL),('isol_package','00','Other',NULL),('isol_package','16','C16',NULL),('isol_package','19','D19/C19',NULL),('isol_package','21','D21/C21',NULL),('isol_package','22','D22',NULL),('isol_package','25','C25',NULL),('isol_package','27','C27',NULL),('isol_package','28','C28',NULL),('isol_package','37','WR137',NULL),('isol_package','38','C38',NULL),('isol_package','75','WR75',NULL),('isol_titles','<br />Description','text',5),('isol_titles','Band','select',1),('isol_titles','Mfr','select',6),('isol_titles','Mfr P/N','text',7),('isol_titles','Package','select',3),('isol_titles','Power(Watts)','text',2),('isol_titles','Type','select',4),('isol_type','CI','Coaxial Isolator',NULL),('isol_type','DC','Drop In Cirulator',NULL),('isol_type','DI','Drop In Isolator',NULL),('isol_type','FF','SNA Female/Female',NULL),('isol_type','WC','Waveguide Circulator',NULL),('isol_type','WI','Waveguide Isolator',NULL),('metal_f1','A','Anodized',NULL),('metal_f1','C','Painted',NULL),('metal_f1','I','Irridite',NULL),('metal_f1','P','Plated',NULL),('metal_f1','R','Raw',NULL),('metal_f2','B','Black',NULL),('metal_f2','C','Clear',NULL),('metal_f2','D','Desert Tan',NULL),('metal_f2','G','Green',NULL),('metal_f2','L','Blue',NULL),('metal_f2','N','Nickel',NULL),('metal_f2','R','Raw',NULL),('metal_f2','S','Silver',NULL),('metal_f2','W','White',NULL),('metal_f2','Y','Yellow',NULL),('metal_titles','Description','text',5),('metal_titles','Finsh F1','select',1),('metal_titles','Finsh F2','select',2),('metal_titles','Option','select',3),('metal_titles','Revision','select',4),('metal_titles','SeqN','label',6),('mfr','mfr','html options',NULL),('M_F','F','Female',NULL),('M_F','M','Male',NULL),('M_F','O','Other',NULL),('M_F','S','Spring',NULL),('pcb_as_title','Description','text',4),('pcb_as_title','Details','select',2),('pcb_as_title','From Bare Board','select',1),('pcb_as_title','Revision','select',3),('plastic_titles','<br />Description','text',4),('plastic_titles','For','select',2),('plastic_titles','Mfr','select',5),('plastic_titles','Mfr P/N','text',6),('plastic_titles','SeqN','label',3),('plastic_titles','Type','select',1),('plastic_titles0','BT','Button',1),('plastic_titles0','HR','Halo Ring',3),('plastic_titles0','LP','Light Pipe',4),('plastic_titles0','PL','Plug',2),('plastic_titles1','00','Nondescript',100),('plastic_titles1','BN','BNC-Type',4),('plastic_titles1','FT','F-Type ',2),('plastic_titles1','NT','N-Type ',3),('plastic_titles1','SA','SMA',1),('plastic_titles2','00','Nondescript',100),('plastic_titles3','00','Nondescript',100),('plastic_titles4','00','Nondescript',100),('po_page','0','Purchase Order',NULL),('po_page','1','Components Movement',NULL),('po_page','2','Vendor',NULL),('po_page','3','Co Manufacture',NULL),('precision','A','(A) 10%',NULL),('precision','B','2%',NULL),('precision','E','5%',NULL),('precision','T','(T) 20%',NULL),('prec_res','0','(0) Jamper',NULL),('prec_res','1','1%',NULL),('prec_res','2','2%',NULL),('prec_res','5','5%',NULL),('prec_res','D','(D) 0.5%',NULL),('prec_res','J','(J) 10%',NULL),('ps_input','12','12',NULL),('ps_input','24','24',NULL),('ps_input','36','9-36 VDC',NULL),('ps_input','3A','300',NULL),('ps_input','48','48',NULL),('ps_input','87','18~75 VDC ',NULL),('ps_input','NC','not PFC',NULL),('ps_input','PF','PFC',NULL),('ps_outputs','A','1',NULL),('ps_outputs','B','2',NULL),('ps_outputs','C','3',NULL),('ps_outputs','D','4',NULL),('ps_out_v','5',' 5 Volts',1),('ps_out_v','C','12 Volts',2),('ps_out_v','D','15 Volts',4),('ps_out_v','E','28 Volts',6),('ps_out_v','F','24 Volts',5),('ps_out_v','G','48 Volts',7),('ps_out_v','I','300 Volts',8),('ps_out_v','W','12/24/48 Volts',3),('ps_package','BM','Board Mounted',NULL),('ps_package','EN','Enclosed',NULL),('ps_package','FA','Front End',NULL),('ps_package','FB','Full Brick',NULL),('ps_package','HB','1/2 Brick',NULL),('ps_package','MB','1/16 Brick',NULL),('ps_package','OB','1/8 Brick',NULL),('ps_package','OF','Open Frame',NULL),('ps_package','QB','1/4 Brick ',NULL),('ps_type','AD','AC/DC',NULL),('ps_type','DD','DC/DC',NULL),('resistor_titles','<br />Description','text',4),('resistor_titles','Mfr','select',5),('resistor_titles','Mfr P/N','text',6),('resistor_titles','Precision(%)','select',2),('resistor_titles','Size','select',3),('resistor_titles','Value(Ohm)','text',1),('rm_wire_titles','<br />Description','text',6),('rm_wire_titles','Form','select',3),('rm_wire_titles','Gauge','select',2),('rm_wire_titles','Insulation','select',4),('rm_wire_titles','Mfr','select',7),('rm_wire_titles','Mfr P/N','text',8),('rm_wire_titles','SeqN','label',5),('rm_wire_titles','Wires Number','select',1),('scr_number','000','No 0',0),('scr_number','002','No 2',2),('scr_number','004','No 4',4),('scr_number','006','No 6',6),('scr_number','007','No 7',7),('scr_number','008','No 8',8),('scr_number','010','No 10',10),('scr_number','038','No 3/8',1),('scr_number','M02','M2',22),('scr_number','M03','M3',23),('scr_number','M04','M4',24),('scr_number','M05','M5',25),('scr_titles','Screw Number','select',1),('scr_titles','SeqN','label',2),('shaw_exel','PCA','PCB Assembled',NULL),('shipping','info','XXX-333-666',NULL),('size','02','0201',NULL),('size','04','0402',NULL),('size','06','0603',NULL),('size','08','0805',NULL),('size','09','0908',NULL),('size','10','1008',NULL),('size','12','1206',NULL),('size','16','1608',NULL),('size','18','1806/1812',NULL),('size','20','2010',NULL),('size','21','2211',NULL),('size','22','2220',NULL),('size','25','2512',NULL),('size','29','2920',NULL),('size','40','4032/4035',NULL),('size','50','5050',NULL),('size','OT','Other',NULL),('size_res','0201','0201',NULL),('size_res','0402','0402',NULL),('size_res','0603','0603',NULL),('size_res','0612','0612',NULL),('size_res','0805','0805',NULL),('size_res','0908','0908',NULL),('size_res','0SMT','SMT',NULL),('size_res','1008','1008',NULL),('size_res','1206','1206',NULL),('size_res','1210','1210',NULL),('size_res','1608','1608',NULL),('size_res','1806','1806',NULL),('size_res','1812','1812',NULL),('size_res','2010','2010',NULL),('size_res','2512','2512',NULL),('size_res','2920','2920',NULL),('size_res','4032','4032',NULL),('size_res','4035','4035',NULL),('size_res','5050','5050',NULL),('size_res','OTHR','Other',NULL),('size_res','THRU','Thru hole',NULL),('tax','GST','5',NULL),('tax','QST','9.975',NULL),('title','Mfr P/N','text',53),('titles','<br />Description','text',51),('titles','Mfr','select',52),('titles','Mfr P/N','text',53),('top_config','0','Regular',NULL),('top_config','1','DC PS',NULL),('top_device','A','Gallium arsenide (GaAs)',NULL),('top_device','S','Gallium nitride (GaN)',NULL),('top_pack','BR','Brick',NULL),('top_pack','HM','Hub Mount',NULL),('top_pack','RM','Rack Mount',NULL),('top_titles','<br />Description','text',7),('top_titles','Band','select',1),('top_titles','Configuration','select',5),('top_titles','Device','select',4),('top_titles','Package','select',3),('top_titles','Power(0.1dBm)','text',2),('top_titles','Revision','select',6),('tr_type','HB','InGaP HBT',NULL),('tr_type','MF','MOSFET',NULL),('tr_type','NP','NPN',NULL),('tr_type','PN','PNP',NULL),('wire_titles','<br />Description','text',4),('wire_titles','Length','text',2),('wire_titles','Mfr','select',5),('wire_titles','Mfr P/N','text',6),('wire_titles','SeqN','label',3),('wire_titles','Wires Number','text',1);
/*!40000 ALTER TABLE `arrays` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:16:04