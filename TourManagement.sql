USE [master]
GO
/****** Object:  Database [TourManagement]    Script Date: 3/7/2017 6:03:50 PM ******/
CREATE DATABASE [TourManagement]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'TourManagement', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA\TourManagement.mdf' , SIZE = 3072KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'TourManagement_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL11.MSSQLSERVER\MSSQL\DATA\TourManagement_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [TourManagement] SET COMPATIBILITY_LEVEL = 110
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [TourManagement].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [TourManagement] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [TourManagement] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [TourManagement] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [TourManagement] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [TourManagement] SET ARITHABORT OFF 
GO
ALTER DATABASE [TourManagement] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [TourManagement] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [TourManagement] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [TourManagement] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [TourManagement] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [TourManagement] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [TourManagement] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [TourManagement] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [TourManagement] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [TourManagement] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [TourManagement] SET  DISABLE_BROKER 
GO
ALTER DATABASE [TourManagement] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [TourManagement] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [TourManagement] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [TourManagement] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [TourManagement] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [TourManagement] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [TourManagement] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [TourManagement] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [TourManagement] SET  MULTI_USER 
GO
ALTER DATABASE [TourManagement] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [TourManagement] SET DB_CHAINING OFF 
GO
ALTER DATABASE [TourManagement] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [TourManagement] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
USE [TourManagement]
GO
/****** Object:  Table [dbo].[Customers]    Script Date: 3/7/2017 6:03:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Customers](
	[CustomerID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NULL,
	[Name] [nvarchar](50) NULL,
	[Email] [nchar](20) NULL,
	[Mobile] [nchar](15) NULL,
	[Address] [nvarchar](50) NULL,
	[PersonalNumber] [nchar](15) NULL,
 CONSTRAINT [PK_Customer] PRIMARY KEY CLUSTERED 
(
	[CustomerID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[TourCate]    Script Date: 3/7/2017 6:03:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TourCate](
	[TourCateID] [int] IDENTITY(1,1) NOT NULL,
	[CateName] [nvarchar](50) NULL,
	[Description] [nvarchar](100) NULL,
 CONSTRAINT [PK_TourCate] PRIMARY KEY CLUSTERED 
(
	[TourCateID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[TourOrders]    Script Date: 3/7/2017 6:03:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TourOrders](
	[OrderID] [int] IDENTITY(1,1) NOT NULL,
	[TourID] [int] NULL,
	[CustomerID] [int] NULL,
	[StartDate] [datetime] NULL,
	[OrderDate] [datetime] NULL,
	[Notes] [nvarchar](100) NULL,
	[Status] [int] NULL,
 CONSTRAINT [PK_TourOrder] PRIMARY KEY CLUSTERED 
(
	[OrderID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Tours]    Script Date: 3/7/2017 6:03:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Tours](
	[TourID] [int] IDENTITY(1,1) NOT NULL,
	[TourCateID] [int] NULL,
	[Name] [nvarchar](50) NULL,
	[ImageUrl] [nchar](60) NULL,
	[Price] [float] NULL,
	[Description] [nvarchar](100) NULL,
 CONSTRAINT [PK_Tour] PRIMARY KEY CLUSTERED 
(
	[TourID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Users]    Script Date: 3/7/2017 6:03:50 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[Username] [nchar](10) NULL,
	[Password] [nchar](10) NULL,
	[RoleNo] [int] NULL,
	[Status] [int] NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET IDENTITY_INSERT [dbo].[TourCate] ON 

INSERT [dbo].[TourCate] ([TourCateID], [CateName], [Description]) VALUES (1, N'Đạp xe', N'Đạp xe đạp quanh thành phố')
SET IDENTITY_INSERT [dbo].[TourCate] OFF
USE [master]
GO
ALTER DATABASE [TourManagement] SET  READ_WRITE 
GO
