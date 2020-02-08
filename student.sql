/*
Navicat SQL Server Data Transfer

Source Server         : sqlserver_local
Source Server Version : 130000
Source Host           : 127.0.0.1:1433
Source Database       : STUTEST_YZC
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 130000
File Encoding         : 65001

Date: 2019-06-15 17:13:05
*/


-- ----------------------------
-- Table structure for STUDENT
-- ----------------------------
DROP TABLE [dbo].[STUDENT]
GO
CREATE TABLE [dbo].[STUDENT] (
[SNO] int NOT NULL IDENTITY(1001,1) ,
[SNAME] varchar(20) NOT NULL ,
[GENDER] varchar(2) NOT NULL ,
[BIRTH] date NULL ,
[PHOTO_URL] varchar(50) NULL 
)


GO
DBCC CHECKIDENT(N'[dbo].[STUDENT]', RESEED, 1014)
GO

-- ----------------------------
-- Records of STUDENT
-- ----------------------------
SET IDENTITY_INSERT [dbo].[STUDENT] ON
GO
INSERT INTO [dbo].[STUDENT] ([SNO], [SNAME], [GENDER], [BIRTH], [PHOTO_URL]) VALUES (N'1001', N'迪丽热巴', N'女', N'1994-06-10', N'upload/688a761f2f73472ea1b00b6ca76b482d.jpg')
GO
GO
INSERT INTO [dbo].[STUDENT] ([SNO], [SNAME], [GENDER], [BIRTH], [PHOTO_URL]) VALUES (N'1002', N'吴亦凡', N'男', N'1989-10-10', N'upload/5a20c7e4ba6748fa9f7668a341dfa6c9.jpg')
GO
GO
INSERT INTO [dbo].[STUDENT] ([SNO], [SNAME], [GENDER], [BIRTH], [PHOTO_URL]) VALUES (N'1003', N'王芳芳', N'女', N'1997-01-10', N'upload/f73e947ed99940a9a573c2ec9e2ce272.jpg')
GO
GO
SET IDENTITY_INSERT [dbo].[STUDENT] OFF
GO

-- ----------------------------
-- Indexes structure for table STUDENT
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table STUDENT
-- ----------------------------
ALTER TABLE [dbo].[STUDENT] ADD PRIMARY KEY ([SNO])
GO
